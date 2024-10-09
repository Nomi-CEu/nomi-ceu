import fs from "fs";
import upath from "upath";
import { configFolder, rootDirectory, templatesFolder } from "#globals";
import mustache from "mustache";
import gulp from "gulp";
import dedent from "dedent-js";
import { isEnvVariableSet } from "#utils/util.ts";
import { BuildData } from "#types/transformFiles.ts";
import { logWarn } from "#utils/log.ts";
import sortKeysRecursive from "sort-keys-recursive";

// This updates all the files, for a release.

// If it is not a release or build, and thus no changes to versions need to be made.
// This occurs when the files are to be updated from the templates outside of a release or a build.
let updateFiles: boolean;
let updateFileVersion: string;
let updateFileTransformedVersion: string;

let buildData: BuildData;

async function updateFilesSetup(): Promise<void> {
	updateFiles = false;
	// See if current run is to update files
	if (isEnvVariableSet("UPDATE_FILES")) {
		try {
			updateFiles = JSON.parse(
				(process.env.UPDATE_FILES ?? "false").toLowerCase(),
			);
		} catch (err) {
			throw new Error("Update Files Env Variable set to Invalid Value.");
		}
	}
	buildData = new BuildData();

	const versionsFilePath: string = upath.join(templatesFolder, "versions.txt");
	updateFileVersion = "";

	if (!buildData.isVersionBuild() && !updateFiles) return;

	// Versions.txt handling
	if (!fs.existsSync(versionsFilePath)) {
		if (updateFiles) {
			if (!buildData.isVersionBuild())
				throw new Error(
					"In order to update files, needs versions.txt to exist and have values, or the version to be set via the GITHUB_TAG environmental variable.",
				);
			updateFileVersion = buildData.rawVersion;
			updateFileTransformedVersion = buildData.transformedVersion;
			return;
		}
		logWarn(
			"Version.txt does not exist. Creating empty file. This may be an error.",
		);

		// Create Versions.txt
		fs.closeSync(fs.openSync(versionsFilePath, "w"));
	} else {
		const versionList = await fs.promises.readFile(versionsFilePath, "utf8");

		if (updateFiles) {
			if (!versionList) {
				if (!buildData.isVersionBuild())
					throw new Error(
						"In order to update files, needs versions.txt to exist and have values, or the version to be set via the GITHUB_TAG environmental variable.",
					);
				updateFileVersion = buildData.rawVersion;
				updateFileTransformedVersion = buildData.transformedVersion;
				return;
			}
			updateFileVersion = versionList.split("\n")[0].replace("-", "").trim();
			updateFileTransformedVersion = `v${updateFileVersion}`;
			return;
		}

		// Duplicate Key
		if (versionList.includes(`${buildData.rawVersion}\n`)) {
			throw new Error("Version already exists in version.txt. Exiting...");
		}
	}
}

export async function updateFilesBuildSetup() {
	updateFiles = true;
	buildData = new BuildData();
	updateFileVersion = buildData.rawVersion;
	updateFileTransformedVersion = buildData.transformedVersion;
}

/**
 * @param readPath The filepath to read from. (Template)
 * @param writePath The filepath to write to.
 * @param replacementObject A record, of type string to type unknown, containing the keys, and replacement for those keys
 * @param addWarning whether to add warning not to edit file
 * <p>
 * <p>
 * A warning not to edit the file will also be added to the start of the file.
 */
async function modifyFile(
	readPath: string,
	writePath: string,
	replacementObject: Record<string, unknown>,
	addWarning = true,
) {
	// Read the file content
	const data: string = await fs.promises.readFile(readPath, "utf8");

	// Moustache Render
	let modifiedData: string = mustache.render(data, replacementObject);

	// Add warning to not edit file
	if (addWarning)
		modifiedData = dedent`# FOR DEVELOPMENT, DO NOT EDIT THIS FILE! EDIT THE TEMPlATES INSTEAD!
			# See https://github.com/Nomi-CEu/Nomi-CEu/wiki/Part-2:-Contributing-Information#section-5-template-information!
			${modifiedData}`;

	// Write the modified content back to the file
	return fs.promises.writeFile(writePath, modifiedData, "utf8");
}

/* Functions NOT Used in Build Process */
async function updateIssueTemplates(): Promise<void> {
	// Filenames
	const fileNames: string[] = ["001-bug-report.yml", "002-feature-request.yml"];

	const versionsFilePath: string = upath.join(templatesFolder, "versions.txt");

	let versionList: string = await fs.promises.readFile(
		versionsFilePath,
		"utf8",
	);

	if (!updateFiles) {
		if (!buildData.isVersionBuild())
			throw new Error(
				"In order to update Issue Templates, the version must be set via the GITHUB_TAG environmental variable.",
			);
		// Add new version to list, with indent
		versionList = `        - ${buildData.rawVersion}\n${versionList}`;

		// Write updated Version List
		await fs.promises.writeFile(versionsFilePath, versionList);
	}

	// Replacement Object
	const replacementObject: Record<string, unknown> = {
		versions: versionList,
	};

	const issueTemplatesFolder: string = upath.join(
		rootDirectory,
		".github",
		"ISSUE_TEMPLATE",
	);

	// Write to issue templates
	for (const fileName of fileNames) {
		const readPath = upath.join(templatesFolder, fileName);
		const writePath = upath.join(issueTemplatesFolder, fileName);
		await modifyFile(readPath, writePath, replacementObject);
	}
}

async function updateMainMenuConfig(): Promise<void> {
	// Filename & paths
	const fileName = "mainmenu.json";
	const readPath: string = upath.join(templatesFolder, fileName);
	const writePath: string = upath.join(
		rootDirectory,
		configFolder,
		"CustomMainMenu",
		fileName,
	);

	if (!updateFiles && !buildData.isVersionBuild())
		throw new Error(
			"The main menu should only be updated if the version is set via the GITHUB_TAG environmental variable.",
		);

	// Replacement object
	const replacementObject: Record<string, unknown> = {
		version: updateFiles ? updateFileVersion : buildData.rawVersion,
	};

	// Read file
	const data: string = await fs.promises.readFile(readPath, "utf8");

	// Moustache Render
	let modifiedData = JSON.parse(mustache.render(data, replacementObject));

	// Add warning to not edit file
	modifiedData["_comment"] =
		"FOR DEVELOPMENT, DO NOT EDIT THIS FILE! EDIT THE TEMPlATES INSTEAD! See https://github.com/Nomi-CEu/Nomi-CEu/wiki/Part-2:-Contributing-Information#section-5-template-information!";

	// Sort keys so that comment appears first
	modifiedData = sortKeysRecursive(modifiedData);

	return await fs.promises.writeFile(
		writePath,
		JSON.stringify(modifiedData, null, 2),
		"utf8",
	);
}

/* Functions USED in Build Process */
export async function updateLabsVersion(rootDir: string): Promise<void> {
	const fileName = "nomilabs-version.cfg";
	const readPath: string = upath.join(templatesFolder, fileName);
	const writePath = upath.join(rootDir, configFolder, fileName);

	const replacementObject: Record<string, unknown> = {
		version: updateFiles
			? updateFileTransformedVersion
			: buildData.transformedVersion,
	};

	return modifyFile(readPath, writePath, replacementObject);
}

// Main Closures
const updateFilesLabsVersion = async () => {
	await updateLabsVersion(rootDirectory);
};

export const updateFilesIssue = gulp.series(
	updateFilesSetup,
	updateIssueTemplates,
);
export const updateFilesVersion = gulp.series(
	updateFilesSetup,
	updateFilesLabsVersion,
);
export const updateFilesMainMenu = gulp.series(
	updateFilesSetup,
	updateMainMenuConfig,
);

export const updateAll = gulp.series(
	updateFilesSetup,
	gulp.parallel(
		updateIssueTemplates,
		updateFilesLabsVersion,
		updateMainMenuConfig,
	),
);
