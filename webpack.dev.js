const path = require('path');
const merge = require('webpack-merge');
const config = require('./webpack.config.js');

module.exports = merge(config, {
    mode: "development",
    devtool: "cheap-eval-source-map",
    devServer: {
        open: true,
        contentBase: path.join(__dirname, 'dist'),
        compress: true,
        port: 9000
      }
})