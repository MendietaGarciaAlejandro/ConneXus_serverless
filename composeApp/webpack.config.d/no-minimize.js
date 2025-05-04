// webpack.config.d/no-minimize.js
module.exports = (config) => {
  config.mode = 'development';            // fuerza mode: development
  config.optimization = config.optimization || {};
  config.optimization.minimize = false;   // desactiva minificación
  return config;
};
