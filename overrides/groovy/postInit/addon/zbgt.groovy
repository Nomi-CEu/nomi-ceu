package postInit.addon.zbgt

import gregtech.api.recipes.builders.AssemblyLineRecipeBuilder
import gregtech.api.recipes.recipeproperties.ResearchProperty
import com.nomiceu.nomilabs.util.LabsModeHelper

import static gregtech.api.GTValues.*

/*
Creative item Recipes
These are recipe for all the ZBGT creative items added
*/

if (LabsModeHelper.normal) {
	//NM Specific

	//Creative Computation
	mods.gregtech.creative_tank_provider.recipeBuilder()
		.notConsumable(item('nomilabs:creativeportabletankmold'))
	//TODO: Add a creative computation mold item through labs or ZBGT
		.inputs(item('minecraft:paper'))
		.outputs(metaitem('zbgt:creative_computation_provider'))
		.duration(500)
		.EUt(1000000)
		.buildAndRegister()

	//Energy Sink
	mods.gregtech.creative_tank_provider.recipeBuilder()
		.notConsumable(metaitem('infinite_energy'))
		.inputs(item('extrautils2:trashcanenergy'))
		.outputs(metaitem('zbgt:creative_energy_sink'))
		.duration(500)
		.EUt(1000000)
		.buildAndRegister()
	//Not Needed anymore
	crafting.removeByInput(metaitem('zbgt:creative_energy_sink'))

	//Energy Source
	mods.gregtech.creative_tank_provider.recipeBuilder()
		.notConsumable(metaitem('infinite_energy'))
		.inputs(item('minecraft:redstone_block'))
		.outputs(metaitem('zbgt:creative_energy_source'))
		.duration(500)
		.EUt(1000000)
		.buildAndRegister()
	//Prevent GT Source dupe
	crafting.removeByInput(metaitem('zbgt:creative_energy_source'))
	//Not needed anymore
	crafting.removeByInput(metaitem('infinite_energy'))
} else {
	//HM Specific

	//Creative Computation
	mods.gregtech.universal_crystallizer.recipeBuilder()
		.notConsumable(item('nomilabs:creativeportabletankmold'))
		.inputs(item('minecraft:paper'))
		.fluidInputs(fluid('naquadria') * 1000000)
		.outputs(metaitem('zbgt:creative_computation_provider'))
		.duration(500)
		.EUt(1000000)
		.buildAndRegister()

	//Energy Sink
	mods.gregtech.universal_crystallizer.recipeBuilder()
		.notConsumable(metaitem('infinite_energy'))
		.inputs(item('extrautils2:trashcanenergy'))
		.fluidInputs(fluid('naquadria') * 1000000)
		.outputs(metaitem('zbgt:creative_energy_sink'))
		.duration(500)
		.EUt(1000000)
		.buildAndRegister()
	crafting.removeByInput(metaitem('zbgt:creative_energy_sink'))

	//Energy Source
	mods.gregtech.universal_crystallizer.recipeBuilder()
		.notConsumable(metaitem('infinite_energy'))
		.inputs(item('minecraft:redstone_block'))
		.fluidInputs(fluid('naquadria') * 1000000)
		.outputs(metaitem('zbgt:creative_energy_source'))
		.duration(500)
		.EUt(1000000)
		.buildAndRegister()
	//Prevent GT Source dupe
	crafting.removeByInput(metaitem('zbgt:creative_energy_source'))
	//Not needed anymore
	crafting.removeByInput(metaitem('infinite_energy'))
}

//Creative Coils
mods.gregtech.precise_assembler_recipes.recipeBuilder()
	.inputs(item('avaritia:resource', 5), item('gregtech:wire_coil', 7) * 512)
	.fluidInputs(fluid('neutronium') * 512000, fluid('pyrotheum') * 20000000)
	.outputs(item('zbgt:creative_heating_coil'))
	.casingTier(4)
	.duration(200).EUt(VA[MAX])
	.buildAndRegister()


/*
Mega multiblocks Recipes
These are recipe for all the ZBGT mega multiblocks in a more balanced way.
Organized by tier in which the script moves them to in NM.
*/

//IV
mods.gregtech.assembler.removeByInput(480, [metaitem('cracker') * 64], [fluid('soldering_alloy') * 9216 * 9216])
mods.gregtech.assembler.removeByInput(480, [metaitem('cracker') * 64], [fluid('tin') * 18432 * 18432])
mods.gregtech.assembler.recipeBuilder()
	.inputs(metaitem('cracker') * 64,
		metaitem('gcym:frameWatertightSteel') * 64,
		ore('circuitLuv') * 16,
		item('gcym:unique_casing', 4) * 16,
		metaitem('electric.pump.iv') * 16,
		metaitem('pipeNormalFluidPolybenzimidazole') * 16
	)
	.fluidInputs(fluid('soldering_alloy') * 9216)
	.outputs(metaitem('zbgt:mega_ocu'))
	.duration(1200).EUt(VA[IV])
	.buildAndRegister()

//ZPM

// Mega Electric Blast Furnace
mods.gregtech.assembler.removeByInput(480, [metaitem('electric_blast_furnace') * 64], [fluid('tin') * 18432 * 18432])
mods.gregtech.assembler.removeByInput(480, [metaitem('electric_blast_furnace') * 64], [fluid('soldering_alloy') * 9216 * 9216])
if (LabsModeHelper.normal) {
	mods.gregtech.assembly_line.recipeBuilder()
		.inputs(metaitem('electric_blast_furnace') * 64,
			metaitem('frameNaquadahAlloy') * 64,
			ore('circuitUv') * 16,
			metaitem('field.generator.luv') * 16,
			metaitem('springNaquadahAlloy') * 16,
			metaitem('plateDenseNaquadahAlloy') * 16,
			metaitem('wireGtHexUraniumRhodiumDinaquadide') * 16
		)
		.fluidInputs(fluid('soldering_alloy') * 9216)
		.outputs(metaitem('zbgt:mega_ebf'))
		.stationResearch(b -> b.researchStack(metaitem('gcym:mega_blast_furnace')).CWUt(16))
		.duration(1200).EUt(VA[LuV])
		.buildAndRegister()
} else {
	mods.gregtech.assembly_line.recipeBuilder()
		.inputs(metaitem('electric_blast_furnace') * 64,
			metaitem('frameTritanium') * 64,
			ore('circuitUhv') * 16,
			metaitem('field.generator.uv') * 16,
			metaitem('springTritanium') * 16,
			metaitem('plateTritanium') * 16,
			metaitem('wireGtHexUraniumRhodiumDinaquadide') * 16
		)
		.fluidInputs(fluid('soldering_alloy') * 9216)
		.outputs(metaitem('zbgt:mega_ebf'))
		.stationResearch(b -> b.researchStack(metaitem('gcym:mega_blast_furnace')).CWUt(128).EUt(VA[UHV]))
		.duration(1200).EUt(VA[UV])
		.buildAndRegister()
}

// Mega Vacuum Freezer
mods.gregtech.assembler.removeByInput(480, [metaitem('vacuum_freezer') * 64], [fluid('tin') * 18432 * 18432])
mods.gregtech.assembler.removeByInput(480, [metaitem('vacuum_freezer') * 64], [fluid('soldering_alloy') * 9216 * 9216])
if (LabsModeHelper.normal) {
	mods.gregtech.assembly_line.recipeBuilder()
		.inputs(metaitem('vacuum_freezer') * 64,
			metaitem('frameNaquadahAlloy') * 64,
			ore('circuitUv') * 16,
			metaitem('field.generator.luv') * 16,
			metaitem('pipeNormalFluidNaquadah') * 16,
			metaitem('plateDenseNaquadahAlloy') * 16,
			metaitem('wireGtHexUraniumRhodiumDinaquadide') * 16
		)
		.fluidInputs(fluid('soldering_alloy') * 9216)
		.outputs(metaitem('zbgt:mega_vf'))
		.stationResearch(b -> b.researchStack(metaitem('gcym:mega_vacuum_freezer')).CWUt(16))
		.duration(1200).EUt(VA[LuV])
		.buildAndRegister()
} else {
	mods.gregtech.assembly_line.recipeBuilder()
		.inputs(metaitem('vacuum_freezer') * 64,
			metaitem('frameDuranium') * 64,
			ore('circuitUhv') * 16,
			metaitem('field.generator.uv') * 16,
			metaitem('pipeNormalFluidDuranium') * 16,
			metaitem('plateDenseTritanium') * 16,
			metaitem('wireGtHexUraniumRhodiumDinaquadide') * 16
		)
		.fluidInputs(fluid('soldering_alloy') * 9216)
		.outputs(metaitem('zbgt:mega_vf'))
		.stationResearch(b -> b.researchStack(metaitem('gcym:mega_vacuum_freezer')).CWUt(128).EUt(VA[UHV]))
		.duration(1200).EUt(VA[UV])
		.buildAndRegister()
}

// Mega Alloy Blast Smelter
mods.gregtech.assembler.removeByInput(480, [metaitem('gcym:alloy_blast_smelter') * 64], [fluid('tin') * 18432 * 18432])
mods.gregtech.assembler.removeByInput(480, [metaitem('gcym:alloy_blast_smelter') * 64], [fluid('soldering_alloy') * 9216 * 9216])
if (LabsModeHelper.normal) {
	mods.gregtech.assembly_line.recipeBuilder()
		.inputs(metaitem('gcym:alloy_blast_smelter') * 64,
			metaitem('nomilabs:frameAwakenedDraconium') * 64,
			ore('circuitUv') * 16,
			metaitem('field.generator.luv') * 16,
			metaitem('nomilabs:ringAwakenedDraconium') * 16,
			metaitem('nomilabs:plateAwakenedDraconium') * 16,
			metaitem('wireGtHexUraniumRhodiumDinaquadide') * 16
		)
		.fluidInputs(fluid('soldering_alloy') * 9216)
		.outputs(metaitem('zbgt:mega_abs'))
		.stationResearch(b -> b.researchStack(metaitem('gcym:alloy_blast_smelter')).CWUt(16))
		.duration(1200).EUt(VA[LuV])
		.buildAndRegister()
} else {
	mods.gregtech.assembly_line.recipeBuilder()
		.inputs(metaitem('gcym:alloy_blast_smelter') * 64,
			metaitem('frameTritanium') * 64,
			ore('circuitUhv') * 16,
			metaitem('field.generator.uv') * 16,
			metaitem('ringTritanium') * 16,
			metaitem('nomilabs:plateTaranium') * 16,
			metaitem('wireGtHexUraniumRhodiumDinaquadide') * 16
		)
		.fluidInputs(fluid('soldering_alloy') * 9216)
		.outputs(metaitem('zbgt:mega_abs'))
		.stationResearch(b -> b.researchStack(metaitem('gcym:alloy_blast_smelter')).CWUt(128).EUt(VA[UHV]))
		.duration(1200).EUt(VA[UV])
		.buildAndRegister()
}
