const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin')
const CopyPlugin = require('copy-webpack-plugin');

module.exports = {
  module: {
    rules: [
      { test: /\.js$/, exclude: /node_modules/, loader: "babel-loader" }
    ]
  },
  entry: './src/game/index.js',
  output: {
    filename: 'main.js',
    path: __dirname + "/src/main/resources/static"
  },
  plugins: [
    new HtmlWebpackPlugin(),
    new CopyPlugin([
      { from: './src/assets', to: './assets' },
    ])
  ]
};