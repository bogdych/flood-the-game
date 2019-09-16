const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin')
const CopyPlugin = require('copy-webpack-plugin');
const FaviconsWebpackPlugin = require('favicons-webpack-plugin');

module.exports = {
  module: {
    rules: [
      { test: /\.js$/, exclude: /node_modules/, loader: "babel-loader" },
      {
        test: /\.css$/i,
        use: ['style-loader', 'css-loader'],
      },
	  {
        test: /\.(png|jpe?g|gif)$/i,
        use: [
          {
            loader: 'file-loader',
          },
        ],
      },
    ]
  },
  entry: './app/game/index.js',
  output: {
    filename: 'main.js',
    path: __dirname + "/src/main/resources/static"
  },
  plugins: [
    new HtmlWebpackPlugin(),
    new CopyPlugin([
      { from: './app/assets', to: './assets' },	
    ]),
	new FaviconsWebpackPlugin('./app/assets/games/flood/favicon.png')
  ]
};