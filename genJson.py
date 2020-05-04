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
    "cracked_bedrock"
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
    "valerium_ingot"
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


def write_block(name):
    write_json("{}{}.json".format(blockModelsDir, name), block_json(name))
    write_json("{}{}.json".format(itemModelsDir, name), item_block_json(name))
    write_json("{}{}.json".format(blockstatesDir, name), blockstate_json(name))


def write_item(name):
    write_json("{}{}.json".format(itemModelsDir, name), item_json(name))


for block in BLOCKS:
    write_block(block)

for item in ITEMS:
    write_item(item)