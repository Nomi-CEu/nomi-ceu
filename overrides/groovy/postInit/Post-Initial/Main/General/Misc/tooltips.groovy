// SIDE: CLIENT
// ^ Script only runs Client-Side

import appeng.core.AEConfig
import appeng.core.features.AEFeature
import com.nomiceu.nomilabs.config.LabsConfig
import com.nomiceu.nomilabs.util.LabsModeHelper
import mustapelto.deepmoblearning.common.metadata.MetadataLivingMatter
import mustapelto.deepmoblearning.common.metadata.MetadataManager
import net.minecraft.item.ItemStack

import static com.nomiceu.nomilabs.groovy.GroovyHelpers.TooltipHelpers.*
import static com.nomiceu.nomilabs.groovy.GroovyHelpers.TranslationHelpers.*
import classes.postInit.Common

/* MC */
// XP Bottle
addTooltip(item('minecraft:experience_bottle'), translatable("nomiceu.tooltip.mc.xp_bottle"))

/* Actually Additions */

// Drill Core
addTooltip(item('actuallyadditions:item_misc', 16), translatable('nomiceu.tooltip.actuallyadditions.drill_core.1'))
if (LabsModeHelper.normal) {
	addTooltip(item('actuallyadditions:item_misc', 16), [translatableEmpty(), translatable('nomiceu.tooltip.actuallyadditions.drill_core.2')])
}

// Canola
addTooltip(item('actuallyadditions:item_misc', 13), [
	translatable('nomiceu.tooltip.actuallyadditions.canola.1'),
	translatable('nomiceu.tooltip.actuallyadditions.canola.2'),
])

// Canola Press
addTooltip(item('actuallyadditions:block_canola_press'), translatable('nomiceu.tooltip.actuallyadditions.canola_press'))

// Knife
addTooltip(item('actuallyadditions:item_knife'), translatable('nomiceu.tooltip.actuallyadditions.knife'))

// Solidified XP
ItemStack solidXp = item('actuallyadditions:item_solidified_experience')
if (LabsModeHelper.normal) {
	addTooltip(solidXp, [
	    translatable('nomiceu.tooltip.actuallyadditions.solidifed_xp.normal.1'),
		translatable('nomiceu.tooltip.actuallyadditions.solidifed_xp.normal.2'),
	])
} else {
	addTooltip(solidXp, translatable('nomiceu.tooltip.actuallyadditions.solidifed_xp.expert'))
}
addTooltip(solidXp, [translatableEmpty(), translatable("nomiceu.tooltip.actuallyadditions.solidifed_xp.amount")])

/* Advanced Rocketry */

// Orbital Laser Drill
addTooltip(item('advancedrocketry:spacelaser'), [
    translatable('nomiceu.tooltip.advancedrocketry.orbital_laser_drill.1'),
	translatable('nomiceu.tooltip.advancedrocketry.orbital_laser_drill.2'),
	translatable('nomiceu.tooltip.advancedrocketry.orbital_laser_drill.3'),
	translatableEmpty(),
	translatable('nomiceu.tooltip.advancedrocketry.orbital_laser_drill.4'),
	translatable('nomiceu.tooltip.advancedrocketry.orbital_laser_drill.5'),
])

// Basic Lens
addTooltip(item('advancedrocketry:lens'), translatable('nomiceu.tooltip.advancedrocketry.basic_lens'))

/* AE2 & NAE2 */

// AE2 Wireless Crafting Terminal (Old Deprecated Items)
for (ItemStack deprecated : [item('ae2wtlib:infinity_booster_card'), item('wct:wct'), item('wct:magnet_card')]) {
	addTooltip(deprecated, [
	    translatable('nomiceu.tooltip.ae2.crafting_terminal_removal.1'),
		translatable('nomiceu.tooltip.ae2.crafting_terminal_removal.2'),
		translatable('nomiceu.tooltip.ae2.crafting_terminal_removal.3'),
	])
}

// Quantum Link Card
addTooltip(item('appliedenergistics2:material', 59), [
    translatable('nomiceu.tooltip.ae2.quantum_link_card.1'),
	translatable('nomiceu.tooltip.ae2.quantum_link_card.2'),
])

// Pattern Expansion Card
addTooltip(item('appliedenergistics2:material', 58), translatable('nomiceu.tooltip.ae2.pattern_expansion_card'))

// Crafting Card
addTooltip(item('appliedenergistics2:material', 53), translatable('nomiceu.tooltip.ae2.crafting_card'))

// Acceleration Card
addTooltip(item('appliedenergistics2:material', 30), translatable('nomiceu.tooltip.ae2.acceleration_card'))

// Magnet Card
addTooltip(item('appliedenergistics2:material', 60), translatable('nomiceu.tooltip.ae2.magnet_card'))

// Inverter Card
addTooltip(item('appliedenergistics2:material', 30), translatable('nomiceu.tooltip.ae2.inverter_card'))

// Fuzzy Card
addTooltip(item('appliedenergistics2:material', 29), translatable('nomiceu.tooltip.ae2.fuzzy_card'))

// Capacity Card
addTooltip(item('appliedenergistics2:material', 27), translatable('nomiceu.tooltip.ae2.capacity_card'))

// Redstone Card
addTooltip(item('appliedenergistics2:material', 26), translatable('nomiceu.tooltip.ae2.acceleration_card'))

// Sticky Card
addTooltip(item('appliedenergistics2:material', 61), translatable('nomiceu.tooltip.ae2.sticky_card'))

// Pure Certus Quartz
addTooltip(item('appliedenergistics2:material', 10), translatable('nomiceu.tooltip.ae2.pure_certus_quartz'))

// Pure Nether Quartz
addTooltip(item('appliedenergistics2:material', 11), translatable('nomiceu.tooltip.ae2.pure_nether_quartz'))

// Pure Fluix
addTooltip(item('appliedenergistics2:material', 12), translatable('nomiceu.tooltip.ae2.pure_fluix'))

// Charged Certus
addTooltip(item('appliedenergistics2:material', 1), translatable('nomiceu.tooltip.ae2.charged_certus'))

/* P2P Tunnels */

// ME P2P (Special)
// Add a new line after its unique tooltip so it is easier to read the global p2p tooltip
addTooltip(Common.meP2p, [
	translatable('nomiceu.tooltip.ae2.p2p.me'),
	translatableEmpty(),
	translatable("nomiceu.tooltip.ae2.p2p.desc"),
])

// Global P2P
for (ItemStack item : Common.p2pVariants) {
	addTooltip(item, translatable("nomiceu.tooltip.ae2.p2p.desc"))
}


// Dense Cables, Conduits, and Controller
// Only apply tooltip if channels not enabled (manually, by players)
if (!AEConfig.instance().isFeatureEnabled(AEFeature.CHANNELS)) {
	// 20..36 = Covered Cables, 40..56 = Smart Cables, 60..76 = Dense Smart Cables, 500..516 = Dense Covered Cables
	for (IntRange range : [20..36, 40..56, 60..76, 500..516]) {
		for (int meta : range) {
			addTooltip(item('appliedenergistics2:part', meta), [
				translatable('nomiceu.tooltip.ae2.channels_not_enabled'),
				translatable('nomiceu.tooltip.ae2.dense'),
			])
		}
	}

	// Dense Conduits
	addTooltip(item('enderio:item_me_conduit', 1), [
		translatable('nomiceu.tooltip.ae2.channels_not_enabled'),
		translatable('nomiceu.tooltip.ae2.dense'),
	])

	// Controller
	addTooltip(item('appliedenergistics2:controller'), [
		translatable('nomiceu.tooltip.ae2.channels_not_enabled'),
		translatable('nomiceu.tooltip.ae2.controller'),
	])
}

// Facades
addTooltip(item('appliedenergistics2:facade'), translatable('nomiceu.tooltip.ae2.facade'))

// Crafting Storages
var craftingStorages = [
	item('appliedenergistics2:crafting_storage_1k'),
	item('appliedenergistics2:crafting_storage_4k'),
	item('appliedenergistics2:crafting_storage_16k'),
	item('appliedenergistics2:crafting_storage_64k'),
	item('nae2:storage_crafting_256k'),
	item('nae2:storage_crafting_1024k'),
	item('nae2:storage_crafting_4096k'),
	item('nae2:storage_crafting_16384k'),
]
for (ItemStack crafting : craftingStorages) {
	addTooltip(crafting, translatable('nomiceu.tooltip.ae2.crafting_storage'))
}

// Storage Cells
var storageCells = [
	item('appliedenergistics2:storage_cell_1k'),
	item('appliedenergistics2:storage_cell_4k'),
	item('appliedenergistics2:storage_cell_16k'),
	item('appliedenergistics2:storage_cell_64k'),
	item('appliedenergistics2:fluid_storage_cell_1k'),
	item('appliedenergistics2:fluid_storage_cell_4k'),
	item('appliedenergistics2:fluid_storage_cell_16k'),
	item('appliedenergistics2:fluid_storage_cell_64k'),
	item('nae2:storage_cell_void'),
	item('nae2:fluid_storage_cell_void'),
	item('nae2:storage_cell_256k'),
	item('nae2:storage_cell_1024k'),
	item('nae2:storage_cell_4096k'),
	item('nae2:storage_cell_16384k'),
	item('nae2:storage_cell_fluid_256k'),
	item('nae2:storage_cell_fluid_1024k'),
	item('nae2:storage_cell_fluid_4096k'),
	item('nae2:storage_cell_fluid_16384k'),
]
for (ItemStack storage : storageCells) {
	addTooltip(storage, [
		translatableEmpty(),
		translatable('nomiceu.tooltip.ae2.storage_cell'),
	])
}

// Quartz Knives
for (ItemStack knife : [item('appliedenergistics2:nether_quartz_cutting_knife'), item('appliedenergistics2:certus_quartz_cutting_knife')]) {
	addTooltip(knife, translatable('nomiceu.tooltip.ae2.quartz_knife'))
}

/* Dimensional Edibles */

// Island Cake
addTooltip(item('dimensionaledibles:island_cake'), [
    translatable('nomiceu.tooltip.dimensionaledibles.island_cake.1'),
	translatable('nomiceu.tooltip.dimensionaledibles.island_cake.2'),
])

/* Dark Utils */

// Ender Tether
addTooltip(item('darkutils:ender_tether'), translatable('nomiceu.tooltip.darkutils.ender_tether'))

/* Deep Mob Evolution */

// Glitch Fragment
addTooltip(item('deepmoblearning:glitch_fragment'), translatable('nomiceu.tooltip.dme.glitch_fragment'))

// Matter
for (MetadataLivingMatter matter : MetadataManager.livingMatterMetadataList) {
	// XP is as a Percent of One Level
	int xpPercent = (matter.xpValue / LabsConfig.advanced.aaEioLinearXp) * 100
	if (xpPercent == 100)
		addTooltip(matter.itemStack, translatable('nomiceu.tooltip.dme.matter.full_level'))
	else
		addTooltip(matter.itemStack, translatable('nomiceu.tooltip.dme.matter', xpPercent))
}

/* Thermal Expansion */

// Capacitors
for (int meta in 0..5) {
	addTooltip(item('thermalexpansion:capacitor', meta), translatable('nomiceu.tooltip.thermalexpansion.capacitors'))
}

/* GregTech */

// Prospectors
for (ItemStack prospector in [metaitem('prospector.lv'), metaitem('prospector.hv'), metaitem('prospector.luv')]) {
	addTooltip(prospector, [
		translatableEmpty(),
		translatable('nomiceu.tooltip.gregtech.prospector.1'),
		translatable('nomiceu.tooltip.gregtech.prospector.2'),
		translatable('nomiceu.tooltip.gregtech.prospector.3'),
		translatable('nomiceu.tooltip.gregtech.prospector.4'),
	])
}

// Facades
addTooltip(metaitem('cover.facade'), [
	translatable('nomiceu.tooltip.gregtech.facade.1'),
	translatable('nomiceu.tooltip.gregtech.facade.2'),
])

/* Ender IO */

// Glasses
addTooltip(item('enderio:block_fused_glass'), translatable('nomiceu.tooltip.eio.fused_glass.make'))

for (ItemStack stack in Common.eioGlasses) {
	addTooltip(stack, translatable('nomiceu.tooltip.eio.glass.dye'))
}

/* Project Red */

// Transmission Wire
addTooltip(item('projectred-transmission:wire'), translatable('nomiceu.tooltip.projectred.wire'))

/* XTones */

// Lamp
addTooltip(item('xtones:lamp_flat'), translatable('nomiceu.tooltip.xtones.lamp'))
