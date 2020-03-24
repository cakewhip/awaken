import sys
import os

#################################################################################
# Configuration section. Modify this to suit your mod. Run this python script
# from within the root of your mod project. Directories in this configuration
# are relative to that root

# This is the actual modid as used in json for example
MODID = 'terminus'

# The relative path to the root of your asset resources
ASSET_RESOURCE_ROOT = './src/main/resources/assets/' + MODID
# The relative path to the root of your data resources
DATA_RESOURCE_ROOT = './src/main/resources/data/' + MODID

#################################################################################
# Template section. You can modify these if you want to personalize how code
# is generated. ${xxx} are input parameters. $U{xxx} will generate an uppercase
# version of the input. $L{xxx} a lowercase version. The following parameters
# are given to the templates:
#      - modid_ref (contains the value of MODID_REF above)
#      - modid (contains the value of MODID above)
#      - name (contains the name of the block to generate (same as the parameter given to this script)
#      - package (contains the package where the code will be geneated)
# Lines enclosed with ?{xxx and ?}xxx
# are conditionally generated depending on input parameters

TEMPLATE_BLOCKSTATE_JSON = '''
{
    "variants": {
        "": { "model": "${modid}:block/$L{name}" }
    }
}
'''

TEMPLATE_BLOCKMODEL_JSON = '''
{
    "parent": "block/cube_all",
    "textures": {
        "all": "${modid}:block/$L{name}"
    }
}
'''

TEMPLATE_ITEMMODEL_JSON = '''
{
  "parent": "${modid}:block/$L{name}"
}
'''

TEMPLATE_LOOTTABLE_JSON = '''
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "${modid}:$L{name}"
        }
      ],
      "conditions": [
        {
          "condition": "minecraft:survives_explosion"
        }
      ]
    }
  ]
}
'''

TEMPLATE_RECIPE_JSON = '''
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "ccc",
    "ccc",
    "ccc"
  ],
  "key": {
    "c": {
      "item": "minecraft:clay"
    }
  },
  "result": {
    "item": "${modid}:$L{name}"
  }
}'''


#################################################################################

def generate(template, inputs):
    for inp, val in inputs.items():
        template = template.replace('${' + inp + '}', val)
        template = template.replace('$U{' + inp + '}', val.upper())
        template = template.replace('$L{' + inp + '}', val.lower())

    return template.strip()

def add_templated_json(path, package, name, template):
    for p in package.split('.'):
        path = os.path.join(path, p)
    print(path)
    os.makedirs(path, exist_ok=True)
    json_name = name.lower() + '.json'
    path = os.path.join(path, json_name)

    print(f'Generated {json_name!r}')
    f = open(path, 'w')

    f.write(
        generate(
            template,
            {
                'modid': MODID,
                'name': name
            }
        )
    )

    f.close()

def add_block(name):
    add_templated_json(ASSET_RESOURCE_ROOT, 'blockstates', name, TEMPLATE_BLOCKSTATE_JSON)
    add_templated_json(ASSET_RESOURCE_ROOT, 'models.block', name, TEMPLATE_BLOCKMODEL_JSON)
    add_templated_json(ASSET_RESOURCE_ROOT, 'models.item', name, TEMPLATE_ITEMMODEL_JSON)
    add_templated_json(DATA_RESOURCE_ROOT, 'loot_tables.blocks', name, TEMPLATE_LOOTTABLE_JSON)
    add_templated_json(DATA_RESOURCE_ROOT, 'recipes', name, TEMPLATE_RECIPE_JSON)

names = sys.argv[1:]

for name in names:
    add_block(name)