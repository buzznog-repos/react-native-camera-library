/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import { Dimensions, TouchableOpacity, Image} from 'react-native';
import { colors } from './config';
import Permissions from 'react-native-permissions'
import {
  Platform,
  StyleSheet,
  Text,
  View
} from 'react-native';
import { CameraRollView } from './components/molecules';
const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
  android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});
import moment from 'moment';
import 'moment-duration-format';

var {
  width: deviceWidth,
  height: deviceHeight
} = Dimensions.get('window')

export default class App extends Component<{}> {
  state = {
    cameraPermission: "",
    photoPermission: ""
  }

  
  componentDidMount() {
    if (Platform.OS === 'android') {
      this._checkCameraAndPhotos();
    } else {
      this.setState({ cameraPermission: "authorized", photoPermission: "authorized"});
    }
  }
  selectImage(asset) {
  }

  _requestPhotoPermission = () => {
    Permissions.request('photo').then(response => {
      this.setState({ photoPermission: response })
    })
  }
  _requestCameraPermission = () => {
    Permissions.request('camera').then(response => {
      this.setState({ cameraPermission: response })
    })
  }

  // Check the status of multiple permissions
  _checkCameraAndPhotos = () => {
    Permissions.checkMultiple(['camera', 'photo']).then(response => {
      //response is an object mapping type to permission
      
      this.setState({
        cameraPermission: response.camera,
        photoPermission: response.photo,
      })
      if(response.photo === "undetermined") {
        this._requestPhotoPermission();
      }
      if(response.camera === "undetermined") {
        this._requestCameraPermission();
      }
    })
  }
  renderCameraRollMedia(asset) {
    var base64Image = 'data:image/png;base64,' + asset.thumbnail;

    return (
      <TouchableOpacity style={{ padding: 2 }} onPress={() => this.selectImage(asset)}>
        <Image style={styles.asset} source={{ uri: base64Image }}>
          {!!asset.duration &&
            <View style={styles.durationTextContainer}>
              <Text style={styles.durationText} >{moment.duration(asset.duration, "seconds").format("m:ss", { trim: false })}</Text>
            </View>
          }
        </Image>
      </TouchableOpacity>
    );
  }

  render() {
    let photoPermission = this.state.photoPermission;
    return (
      <View style={styles.container}>
      {
        photoPermission === "authorized" &&
        <CameraRollView
          assetType='Photos'
          renderCameraRollMedia={(asset) => this.renderCameraRollMedia(asset)}
          style={{ position: 'absolute', top: deviceHeight * 0.6, bottom: 0, left: -2, right: -2, backgroundColor: colors.grayDarker }}
          contentContainerStyle={{ marginTop: 2, flexDirection: 'row', flexWrap: 'wrap' }}
          loadingAnimationContainerStyle={styles.loadingAnimationContainer}
        />
      }
        
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  asset: {
    width: deviceWidth * 0.3333333333 - 2.75,
    height: deviceWidth * 0.3333333333 - 2.75,
  },
  durationTextContainer: {
    position: 'absolute',
    bottom: 4,
    right: 4,
    backgroundColor: 'rgba(255,255,255,0.75)',
    paddingVertical: 2,
    paddingHorizontal: 4,
    borderRadius: 2
  },
  durationText: {
    fontSize: 10,
    fontWeight: '500'
  },
  loadingAnimationContainer: {
    flex: 1,
    height: deviceWidth * 0.3333333333 - 2.75,
    alignItems: 'center',
    justifyContent: 'center',
  }
});
