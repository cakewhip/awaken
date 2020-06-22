BLOCKS = [
    "ancient_coal_ore",
    "ancient_iron_ore",
    "ancient_gold_ore",
    "ancient_redstone_ore",
    "ancient_lapis_ore",
    "ancient_diamond_ore",
    "ancient_emerald_ore",
    "ancient_salvium_ore",
    "ancient_valerium_ore",
    "cracked_bedrock",
    "null_stone",
    "ancient_stone",
    "ancient_cobblestone",
    "null_stone_bricks",
    "glowing_null_stone_bricks",
    "corrupted_null_stone_bricks"
]


ITEMS = [
    "necrotic_heart",
    "creeper_tissue",
    "spider_silk",
    "bone_marrow",
    "fiery_heart",
    "smoldering_heart",
    "fiery_core",
    "cindered_soul",
    "magma_strand",
    "primordial_core",
    "frigid_core",
    "tarnished_core",
    "wild_core",
    "earth_core",
    "abominable_amalgam",
    "void_crux",
    "corsair_token",
    "raptor_chicken_egg",
    "salvium_ingot",
    "valerium_ingot",
    "fortified_stick",
    "adult_bib",
    "anchor_charm",
    "anglers_tackle_box",
    "baby_bib",
    "bone_crown",
    "boxing_gloves",
    "breastplate_stretcher",
    "broken_ankle_chain",
    "broken_ankle_weights",
    "bycocket",
    "carnivorous_mask",
    "combat_saddle",
    "discord_belt",
    "dragon_tooth_necklace",
    "dynamite_stick",
    "electrifying_dynamite",
    "farmers_hankerchief",
    "glacial_aglet",
    "glacial_weights",
    "guarded_aglet",
    "guarded_chain",
    "lightning_bottle",
    "lucky_tackle",
    "magma_visor",
    "netherian_belt",
    "rain_hat",
    "rangers_glove",
    "scorched_mask",
    "shockwave_shield",
    "shulker_charm",
    "shulker_glove",
    "silky_glove",
    "snorkel_mask",
    "steel_gauntlet"
]


TOOLS = [
    "valerium_sword",
    "salverium_pickaxe"
]


BOWS = [
    "slimey_bow",
    "cindered_bow",
    "salvium_bow"
]


modelsDir = "src/main/resources/assets/awaken/models/"
itemModelsDir = modelsDir + "item/"
blockModelsDir = modelsDir + "block/"
blockstatesDir = "src/main/resources/assets/awaken/blockstates/"


def write_json(path, json):
    f = open(path, "w")
    f.write(json)
    f.close()


def block_json(name):
    return """{{
    \"parent\": \"block/cube_all\",
    \"textures\": {{
        \"all\": \"awaken:block/{}\"
    }}
}}""".format(name)


def item_block_json(name):
    return """{{
  \"parent\": \"awaken:block/{}\"
}}""".format(name)


def blockstate_json(name):
    return """{{
    \"variants\": {{
        \"\": {{ \"model\": \"awaken:block/{}\" }}
    }}
}}""".format(name)


def item_json(name):
    return """{{
  \"parent\": \"item/generated\",
  \"textures\": {{
    \"layer0\": \"awaken:item/{}\"
  }}
}}""".format(name)


def tool_json(name):
    return """{{
  \"parent\": \"minecraft:item/handheld\",
  \"textures\": {{
    \"layer0\": \"awaken:item/{}\"
  }}
}}""".format(name)


def bow_json(name):
    return """{{
  \"parent\": \"item/generated\",
  \"textures\": {{
    \"layer0\": \"awaken:item/{}\"
  }},
  \"display\": {{
    \"thirdperson_righthand\": {{
      \"rotation\": [ -80, 260, -40 ],
      \"translation\": [ -1, -2, 2.5 ],
      \"scale\": [ 0.9, 0.9, 0.9 ]
    }},
    \"thirdperson_lefthand\": {{
      \"rotation\": [ -80, -280, 40 ],
      \"translation\": [ -1, -2, 2.5 ],
      \"scale\": [ 0.9, 0.9, 0.9 ]
    }},
    \"firstperson_righthand\": {{
      \"rotation\": [ 0, -90, 25 ],
      \"translation\": [ 1.13, 3.2, 1.13],
      \"scale\": [ 0.68, 0.68, 0.68 ]
    }},
    \"firstperson_lefthand\": {{
      \"rotation\": [ 0, 90, -25 ],
      \"translation\": [ 1.13, 3.2, 1.13],
      \"scale\": [ 0.68, 0.68, 0.68 ]
    }}
  }},
  \"overrides\": [
    {{
      \"predicate\": {{
        \"pulling\": 1
      }},
      \"model\": \"awaken:item/{}_pulling_0\"
    }},
    {{
      \"predicate\": {{
        \"pulling\": 1,
        \"pull\": 0.65
      }},
      \"model\": \"awaken:item/{}_pulling_1\"
    }},
    {{
      \"predicate\": {{
        \"pulling\": 1,
        \"pull\": 0.9
      }},
      \"model\": \"awaken:item/{}_pulling_2\"
    }}
  ]
}}""".format(name, name, name, name)


def write_block(name):
    write_json("{}{}.json".format(blockModelsDir, name), block_json(name))
    write_json("{}{}.json".format(itemModelsDir, name), item_block_json(name))
    write_json("{}{}.json".format(blockstatesDir, name), blockstate_json(name))


def write_item(name):
    write_json("{}{}.json".format(itemModelsDir, name), item_json(name))


def write_tool(name):
    write_json("{}{}.json".format(itemModelsDir, name), tool_json(name))


def write_bow(name):
    write_json("{}{}.json".format(itemModelsDir, name), bow_json(name))


for block in BLOCKS:
    write_block(block)

for item in ITEMS:
    write_item(item)

for tool in TOOLS:
    write_tool(tool)

for bow in BOWS:
    write_bow(bow)