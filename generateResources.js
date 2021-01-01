/*
    God I love all this boilerplate JSON crap.
 */

const fs = require( "fs" )
const path = require( "path" )
const assetRoot = "./src/generated/resources/assets/rtk"
const dataRoot = "./src/generated/resources/data/rtk"

// ====================================================

{
    generateItemModel( "devtool" )

    generateBlock( "emergencytentlight" )
    const names = ",emergency,divers,ender,diversender".split( "," )
    for ( let name of names ) {
        generateBlock( name + "tent" )
        generateBlock( name + "tentwall" )
    }

    generateRecipeShaped(
        "emergencytent", "rtk:emergencytent",
        "OLO,LCL,OLO",
        { O: "minecraft:orange_dye", L: "minecraft:leather", C: "minecraft:coal" }
    )
    generateRecipeShaped(
        "tent", "rtk:tent",
        "ILI,LEL,ILI",
        { I: "minecraft:iron_ingot", L: "minecraft:leather", E: "rtk:emergencytent" }
    )
}

// ====================================================

function generateBlockItemModel( name ) {
    outputAsset(
        `models/item/${ name }.json`,
        {
            "parent": "rtk:/block/" + name
        }
    )
}

function generateBlockModel( name ) {
    outputAsset(
        `models/block/${ name }.json`,
        {
            "parent": "minecraft:block/cube_all",
            "textures": {
                "all": "rtk:block/" + name
            }
        }
    )
}

function generateBlockState( name ) {
    outputAsset(
        `blockstates/${ name }.json`,
        {
            "variants": {
                "": {
                    "model": "rtk:block/" + name
                }
            }
        }
    )
}

function generateItemModel( name ) {
    outputAsset(
        `models/item/${ name }.json`,
        {
            "parent": "item/generated",
            "textures": {
                "layer0": "rtk:items/" + name
            }
        }
    )
}

function generateBlockDrops( name ) {
    outputData(
        `loot_tables/blocks/${ name }.json`,
        {
            "type": "minecraft:block",
            "pools": [
                {
                    "rolls": 1,
                    "entries": [
                        {
                            "type": "minecraft:item",
                            "name": "rtk:" + name
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
    )
}

function generateBlock( name ) {
    generateBlockState( name )
    generateBlockModel( name )
    generateBlockItemModel( name )
    generateBlockDrops( name )
}

function generateRecipeShaped( name, result, pattern, key ) {
    let pattern2 = pattern
        .split( "," )
        .map( x => x.trim() )
        .filter( x => x.length > 0 )
    let key2 = {}
    for ( let k in key ) key2[ k ] = { item: key[ k ] }
    outputData(
        `recipes/${ name }.json`,
        {
            type: "minecraft:crafting_shaped",
            pattern: pattern2,
            key: key2,
            result: { item: result }
        }
    )
}

function outputAsset( pathStr, jsonObj ) { output( assetRoot, pathStr, jsonObj ) }
function outputData( pathStr, jsonObj ) { output( dataRoot, pathStr, jsonObj ) }
function output( root, pathStr, jsonObj ) {
    let netPath = path.join( root, pathStr )
    fs.mkdirSync( path.dirname( netPath ), { recursive: true } )
    fs.writeFileSync( netPath, JSON.stringify( jsonObj, null, 4 ) )
}