import { QuestBook } from "#types/bqQuestBook.ts";
import fs from "fs";
import {
	cfgExpertPath,
	cfgNormalPath,
	cfgOverrideExpertPath,
	cfgOverrideNormalPath,
	emptyQuest,
	id,
	name,
	stringifyQB,
	stripRewards,
} from "#tasks/helpers/actionQBUtils.ts";
import { input, select } from "@inquirer/prompts";
import { SourceOption } from "#types/actionQBTypes.ts";
import logInfo, { logWarn } from "#utils/log.ts";
import upath from "upath";
import { rootDirectory } from "#globals";
import colors from "colors";

const isAvailableForFormatting = /[0-9a-ek-or]/;

export const check = () => checkAndFix(true);
export const fix = () => checkAndFix(false);

async function checkAndFix(shouldCheck: boolean) {
	logInfo(colors.bold(`${shouldCheck ? "Checking" : "Fixing"} QB...`));
	let checkNormalQB: QuestBook;
	let checkExpertQB: QuestBook;

	if (shouldCheck) {
		const nml1 = await fs.promises.readFile(
			upath.join(rootDirectory, cfgNormalPath),
			"utf-8",
		);
		const nml2 = await fs.promises.readFile(
			upath.join(rootDirectory, cfgOverrideNormalPath),
			"utf-8",
		);
		if (nml1 !== nml2) throw new Error("Normal Quest Books are not the Same!");

		const exp1 = await fs.promises.readFile(
			upath.join(rootDirectory, cfgExpertPath),
			"utf-8",
		);
		const exp2 = await fs.promises.readFile(
			upath.join(rootDirectory, cfgOverrideExpertPath),
			"utf-8",
		);
		if (exp1 !== exp2) throw new Error("Normal Quest Books are not the Same!");

		checkNormalQB = JSON.parse(nml1) as QuestBook;
		checkExpertQB = JSON.parse(exp1) as QuestBook;
	} else {
		const normalSrc = (await select({
			message: "Which version should we use, for the Normal Source File?",
			choices: [
				{
					name: "Main Config Dir",
					value: "CFG" as SourceOption,
				},
				{
					name: "Config Overrides",
					value: "CFG-OVERRIDE" as SourceOption,
				},
			],
		})) as SourceOption;

		const expertSrc = (await select({
			message: "Which version should we use, for the Expert Source File?",
			choices: [
				{
					name: "Main Config Dir",
					value: "CFG" as SourceOption,
				},
				{
					name: "Config Overrides",
					value: "CFG-OVERRIDE" as SourceOption,
				},
			],
		})) as SourceOption;

		checkNormalQB = JSON.parse(
			await fs.promises.readFile(
				upath.join(
					rootDirectory,
					normalSrc === "CFG" ? cfgNormalPath : cfgOverrideNormalPath,
				),
				"utf-8",
			),
		);

		checkExpertQB = JSON.parse(
			await fs.promises.readFile(
				upath.join(
					rootDirectory,
					expertSrc === "CFG" ? cfgExpertPath : cfgOverrideExpertPath,
				),
				"utf-8",
			),
		);
	}

	logInfo(colors.bold("Processing Normal QB..."));
	await checkAndFixQB(shouldCheck, checkNormalQB, false);
	logInfo(colors.bold("Processing Expert QB..."));
	await checkAndFixQB(shouldCheck, checkExpertQB, true);

	if (!shouldCheck) {
		logInfo("Saving...");
		const normal = stringifyQB(checkNormalQB);
		const expert = stringifyQB(checkExpertQB);
		await Promise.all([
			fs.promises.writeFile(upath.join(rootDirectory, cfgNormalPath), normal),
			fs.promises.writeFile(
				upath.join(rootDirectory, cfgOverrideNormalPath),
				normal,
			),
			fs.promises.writeFile(upath.join(rootDirectory, cfgExpertPath), expert),
			fs.promises.writeFile(
				upath.join(rootDirectory, cfgOverrideExpertPath),
				expert,
			),
		]);
	} else logInfo(colors.bold("Successful. No Formatting Errors!"));
}

async function checkAndFixQB(
	shouldCheck: boolean,
	qb: QuestBook,
	isExpert: boolean,
) {
	let index = 0;
	for (const questKey of Object.keys(qb["questDatabase:9"])) {
		let quest = qb["questDatabase:9"][questKey];
		const foundID = id(quest);

		// Check for Missing Quests
		if (foundID !== index) {
			if (shouldCheck) throw new Error(`Missing Quest at Index ${index}!`);
			logWarn(`Adding Empty Quest at Index ${index}...`);
			quest = { ...emptyQuest };
			quest["questID:3"] = index;
			qb["questDatabase:9"][questKey] = quest;
			index++;
			continue;
		}
		index++;

		// Check Name Formatting
		quest["properties:10"]["betterquesting:10"]["name:8"] =
			stripOrThrowExcessFormatting(shouldCheck, name(quest), id(quest), "Name");

		// Check for Empty Descriptions
		if (!quest["properties:10"]["betterquesting:10"]["desc:8"]) {
			if (shouldCheck)
				throw new Error(`Quest has Empty Description at Index ${index}!`);

			quest["properties:10"]["betterquesting:10"]["desc:8"] = await input({
				message: `Quest has an Empty Description at Index ${index}! What should we Replace it With?`,
				default: "No Description",
				validate: (value) => Boolean(value),
			});
		}
		// Check Desc Formatting (Still check if after, as user may have entered dupe formatting)
		quest["properties:10"]["betterquesting:10"]["desc:8"] =
			stripOrThrowExcessFormatting(
				shouldCheck,
				quest["properties:10"]["betterquesting:10"]["desc:8"],
				id(quest),
				"Description",
			);

		// Visibility Check
		if (
			quest["properties:10"]["betterquesting:10"]["visibility:8"] === "NORMAL"
		) {
			if (shouldCheck)
				throw new Error(`Quest has Visibility Normal at Index ${index}!`);

			quest["properties:10"]["betterquesting:10"]["visibility:8"] =
				await select({
					message: `Quest has Visibility Normal at Index ${index}! What should we Replace it With?`,
					choices: [
						{
							name: "Always",
							value: "ALWAYS",
						},
						{
							name: "Chain",
							value: "CHAIN",
						},
						{
							name: "Hidden",
							value: "HIDDEN",
						},
						{
							name: "Unlocked",
							value: "UNLOCKED",
						},
					],
				});
		}

		// Check for Rewards that have Nomicoins
		if (isExpert) stripRewards(quest, isExpert, true);
	}
}

function stripOrThrowExcessFormatting(
	shouldCheck: boolean,
	value: string,
	id: number,
	key: string,
): string {
	if (!value.includes("§")) return value;

	let builder: string[] = [];

	for (let i = 0; i < value.length; i++) {
		const char = value.charAt(i);

		if (builder.at(-1) === "§") {
			if (char === "f") {
				if (shouldCheck)
					throw new Error(
						`Quest with ID ${id} at ${key} has Formatting Code 'f'!`,
					);
				logWarn(
					`Replacing Formatting Code 'f' with 'r' in Quest with ID ${id} at ${key}...`,
				);
				builder.push("r");
				continue;
			}

			if (!isAvailableForFormatting.test(char)) {
				if (shouldCheck)
					throw new Error(
						`Quest with ID ${id} at ${key} has Lone Formatting Signal!`,
					);

				logWarn(
					`Removing Lone Formatting Signal in Quest with ID ${id} at ${key}...`,
				);

				// Remove Last Element
				builder = builder.slice(0, -1);
				continue;
			}

			// Start of String, Remove Formatting is NOT Needed
			if (builder.length === 1 && char === "r") {
				if (shouldCheck)
					throw new Error(
						`Quest with ID ${id} at ${key} has Redundant Formatting!`,
					);

				logWarn(
					`Removing Redundant Formatting from Quest with ID ${id} at ${key}...`,
				);

				// Remove Previous
				builder = [];
				continue;
			}
			builder.push(char);
			continue;
		}

		if (char === "§") {
			// If two characters before was not § (if builder length < 2, `.at` returns undefined)
			if (builder.at(-2) !== "§") {
				builder.push(char);
				continue;
			}

			if (shouldCheck)
				throw new Error(
					`Quest with ID ${id} at ${key} has Redundant Formatting!`,
				);

			logWarn(
				`Removing Redundant Formatting from Quest with ID ${id} at ${key}...`,
			);

			// Remove Previous
			builder = builder.slice(0, -2);
		}

		builder.push(char);
	}
	return builder.join("");
}
