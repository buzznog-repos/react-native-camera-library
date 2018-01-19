
import { NativeModules } from 'react-native';

const NativeCameraLibrary = NativeModules.RNCameraLibrary;

var CameraLibrary = {
  getPhotos: function(params, callback) {
    NativeCameraLibrary.getPhotos(params, callback);
  }
};

module.exports = CameraLibrary;

