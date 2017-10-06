var CopyWebpackPlugin = require('copy-webpack-plugin');
var path = require('path');

module.exports = {
    entry: './src/main/web/route.jsx',
    devtool: 'sourcemaps',
    cache: true,
    output: {
        path: path.join(__dirname, 'src/main/resources/static'),
        filename: 'bundle.js'
    },
    plugins: [
        new CopyWebpackPlugin([
            { from: 'src/main/web/index.html', to: 'index.html'},
            { from: 'src/main/web/favicon.ico', to: 'favicon.ico'}
        ])
    ],
    resolve: {
        extensions: ['.js', '.jsx'],
        modules: [path.join(__dirname, 'src/main/web'), 'node_modules']
    },
    module: {
        rules: [
            {test: path.join(__dirname, 'src/main/web'), loader: 'babel-loader', query: { presets: ['react'] }},
            {test: /\.styl$/, loaders: ['style-loader', 'css-loader', 'stylus-loader']},
            {test: /\.(gif|png|jpg|svg|ttf|woff|woff2|eot)$/, loader: 'url-loader?limit=8192'}
        ]
    },
    devServer: {
        proxy: {
            '*': {
                target: 'http://localhost:8080',
            }
        }
    }
};