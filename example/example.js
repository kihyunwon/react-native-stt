import React, { Component } from 'react';
import {
  ListView,
  StyleSheet,
  Text,
  View,
  TouchableHighlight,
  Platform,
  PermissionsAndroid
} from 'react-native';

import Stt from 'react-native-stt';

export default class Example extends Component {
  constructor(props) {
    super(props);
    this.ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
    this.state = {
      hasPermission: false,
      recording: false,
      text: '',
      langs: []
    };
  }

  componentWillMount() {
    this.subscription =
      Stt.addListener('SpeechToText', (result) => {
        if (result) {
          if (result.error) {
            alert(JSON.stringify(result.error));
          } else if (result.bestTranscription) {
            this.setState({ text: result.bestTranscription.formattedString });
          } else if (result.results) {
            this.setState({ text: result.results });
          }
        }
      });

    this.subscription =
      Stt.addListener('supportedLocales', (result) => {
        if (result) {
          if (result.error) {
            alert(JSON.stringify(result.error));
          } else if (result.supportedLanguages) {
            this.setState({ langs: result.supportedLanguages });
          } else{
            this.setState({ langs: result });
          }
        }
      });
  }

  componentWillUnmount() {
    if (this.subscription != null) {
      this.subscription.remove();
      this.subscription = null;
    }

    if (this.subscription != null) {
      this.subscription.remove();
      this.subscription = null;
    }
    Stt.destroy();
  }

  componentDidMount() {
    this._checkPermission().then((hasPermission) => {
      this.setState({ hasPermission });

      if (!hasPermission) return;
    });
  }

  _checkPermission() {
    if (Platform.OS !== 'android') {
      return Promise.resolve(true);
    }

    const rationale = {
      'title': 'Microphone Permission',
      'message': 'Example needs access to your microphone so you can record audio.'
    };

    return PermissionsAndroid
      .request(PermissionsAndroid.PERMISSIONS.RECORD_AUDIO, rationale)
      .then((result) => {
        return (result === true || result === PermissionsAndroid.RESULTS.GRANTED);
      });
  }

  _renderButton(title, onPress, active) {
    var style = (active) ? styles.activeButtonText : styles.buttonText;

    return (
      <TouchableHighlight style={styles.button} onPress={onPress}>
        <Text style={style}>
          {title}
        </Text>
      </TouchableHighlight>
    );
  }

  async _stop() {
    if (!this.state.recording) {
      return;
    }

    this.setState({ recording: false });

    try {
      Stt.stop();
    } catch (error) {
      console.error(error);
    }
  }

  async _start() {
    if (this.state.recording) {
      console.warn('Already recording!');
      return;
    }

    if (!this.state.hasPermission) {
      console.warn('Can\'t record, no permission granted!');
      return;
    }

    this.setState({ recording: true });

    try {
      Stt.start("");
    } catch (error) {
      console.error(error);
    }
  }

  async _supportedLocales() {
    try {
      Stt.supportedLocales();
    } catch (error) {
      console.error(error);
    }
  }

  render() {
    let dataSource = this.ds.cloneWithRows(this.state.langs);

    return (
      <View style={styles.container}>
        <View style={styles.two}>
          <View style={styles.list}>
            <ListView
              enableEmptySections={true}
              dataSource={dataSource}
              renderRow={(rowData) => {
                return (
                  <Text style={styles.progressText}>{rowData}</Text>
                );
              }}
            />
          </View>
          <View style={styles.controls}>
            {this._renderButton("START", () => {this._start()}, this.state.recording )}
            {this._renderButton("STOP", () => {this._stop()} )}
            {this._renderButton("Languages", () => {this._supportedLocales()} )}
          </View>
        </View>
        <View style={styles.bottom}>
          <Text style={styles.progressText}>{this.state.text}</Text>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#2b608a",
    justifyContent: 'space-around',
    alignItems: 'center'
  },
  two: {
    flex: 1,
    paddingTop: 50,
    flexDirection: 'row',
    justifyContent: 'space-around'
  },
  list: {
    flex: 1,
    paddingBottom: 100,
    alignItems: 'center'
  },
  controls: {
    flex: 1,
    alignItems: 'center'
  },
  bottom: {
    flex: 1
  },
  langText: {
    margin: 10,
    fontSize: 25,
    color: "#fff"
  },
  progressText: {
    textAlign: 'center',
    fontSize: 25,
    color: "#fff"
  },
  button: {
    padding: 20
  },
  buttonText: {
    fontSize: 20,
    color: "#fff"
  },
  activeButtonText: {
    fontSize: 20,
    color: "#B81F00"
  }
});
