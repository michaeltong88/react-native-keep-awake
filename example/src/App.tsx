import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import KeepAwake from 'react-native-keep-awake';

export default function App() {
  React.useEffect(() => {
    KeepAwake.setShowWhenLocked(true);
  }, []);

  return (
    <View style={styles.container}>
      <Text>Show When Locked</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
