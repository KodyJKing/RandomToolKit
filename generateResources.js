const fs = require( "fs" )
const path = require( "path" )
const assetRoot = "./src/generated/resources/assets/rtk"

{
    generateItemModel( "devtool" )

    generateBlock( "tent" )
    generateBlock( "tentwall" )
    generateBlock( "emergencytentwall" )
    generateBlock( "emergencytentlight" )
    generateBlock( "endertentwall" )
    generateBlock( "diverstentwall" )
    generateBlock( "diversendertentwall" )
}

function output( pathStr, jsonObj ) {
    let netPath = path.join( assetRoot, pathStr )
    fs.mkdirSync( path.dirname( netPath ), { recursive: true } )
    fs.writeFileSync( netPath, JSON.stringify( jsonObj, null, 2 ) )
}

function generateBlockItemModel( name ) {
    output(
        `models/item/${ name }.json`,
        {
            "parent": "rtk:/block/" + name
        }
    )
}

function generateBlockModel( name ) {
    output(
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
    output(
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
    output(
        `models/item/${ name }.json`,
        {
            "parent": "item/generated",
            "textures": {
                "layer0": "rtk:items/" + name
            }
        }
    )
}

function generateBlock( name ) {
    generateBlockState( name )
    generateBlockModel( name )
    generateBlockItemModel( name )
}