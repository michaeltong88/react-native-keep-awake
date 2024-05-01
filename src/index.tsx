import { useEffect } from 'react';
import { NativeModules } from 'react-native';

const { KeepAwake } = NativeModules;

let locked = 0;

export const setKeepScreenOn = (isScreenOn: boolean = true) => {
  KeepAwake.setKeepScreenOn(isScreenOn);
};

export const setShowWhenLocked = (locked: boolean) => {
  KeepAwake.setShowWhenLocked(locked);
};

export const setSystemUiVisibility = (lock: boolean) => {
  KeepAwake.setSystemUiVisibility(lock);
};

export const wakeScreen = (isWakeup: boolean = true) => {
  KeepAwake.wakeScreen(isWakeup);
};

export const useKeepAwake = () => {
  useEffect(() => {
    locked ++;
    if (locked <= 1) {
      setKeepScreenOn(true);
    }

    return () => {
      locked --;
      if (!locked) {
        setKeepScreenOn(false);
      }
    };
  }, []);
};

export default () => {
  useEffect(() => {
    locked ++;
    if (locked <= 1) {
      setKeepScreenOn(true);
    }

    return () => {
      locked --;
      if (!locked) {
        setKeepScreenOn(false);
      }
    };
  }, []);

  return null;
};
