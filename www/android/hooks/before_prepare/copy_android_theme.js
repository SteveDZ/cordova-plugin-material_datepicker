#!/usr/bin/env node

module.exports = function(ctx) {
    console.log('ready to execute copy of theme...');

    var path = ctx.requireCordovaModule('path');
    var fs = ctx.requireCordovaModule('fs');

    var platformRoot = path.join(ctx.opts.projectRoot, 'platforms/android');
    var themeRoot = path.join(ctx.opts.plugin.pluginInfo.dir, 'www/android/res');

    var srcFile = path.join(themeRoot, 'styles.xml');
    var destFile = path.join(platformRoot, 'res/values/styles.xml');

    console.log('Copying files...: ' + JSON.stringify(ctx));

    fs.createReadStream(srcFile).pipe(fs.createWriteStream(destFile));
}