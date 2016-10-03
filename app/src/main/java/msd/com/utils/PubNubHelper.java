package msd.com.utils;

import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Pubnub helper class for hte interact functionality
 */
public class PubNubHelper {
    private Pubnub mPubNub;
    public static final String PUBLISH_KEY = "pub-c-61caa62e-ea5d-4292-88ea-de9f27bf88a9";
    public static final String SUBSCRIBE_KEY = "sub-c-b5e4eabc-0ca1-11e6-8c3e-0619f8945a4f";
    public static final String PHUE_CHANNEL = "phue";
    public static final String START_CHANNEL = "start";
    public static final String STOP_CHANNEL = "stop";
    public static final String PLAY_CHANNEL = "play";
    public static final String PAUSE_CHANNEL = "pause";
    public static final String STOP_MUSIC_CHANNEL = "stopMusic";
    public static final String RESUME_CHANNEL = "pause";

    public PubNubHelper() {
        initPubNub();
    }

    public void initPubNub(){
        this.mPubNub = new Pubnub(
                PUBLISH_KEY,
                SUBSCRIBE_KEY
        );
        this.mPubNub.setUUID("AndroidPHue");
        subscribe();
    }

    public void publish(int red, int green, int blue){
        JSONObject js = new JSONObject();
        try {
            js.put("RED",   red);
            js.put("GREEN", green);
            js.put("BLUE",  blue);
        } catch (JSONException e) { e.printStackTrace(); }

        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                Log.d("PUBNUB", response.toString());
            }
            public void errorCallback(String channel, PubnubError error) {
                Log.d("PUBNUB",error.toString());
            }
        };
        this.mPubNub.publish(PHUE_CHANNEL, js, callback);
    }

    public void publishStart(){
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE",   "start LED ");
        } catch (JSONException e) { e.printStackTrace(); }

        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                Log.d("PUBNUB",response.toString());
            }
            public void errorCallback(String channel, PubnubError error) {
                Log.d("PUBNUB",error.toString());
            }
        };
        this.mPubNub.publish(START_CHANNEL, js, callback);
    }

    public void publishStop(){
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE",   "stop LED ");
        } catch (JSONException e) { e.printStackTrace(); }

        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                Log.d("PUBNUB",response.toString());
            }
            public void errorCallback(String channel, PubnubError error) {
                Log.d("PUBNUB",error.toString());
            }
        };
        this.mPubNub.publish(STOP_CHANNEL, js, callback);
    }


    public void publishPlayMusic(){
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE",   "Play Music ");
        } catch (JSONException e) { e.printStackTrace(); }

        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                Log.d("PUBNUB",response.toString());
            }
            public void errorCallback(String channel, PubnubError error) {
                Log.d("PUBNUB",error.toString());
            }
        };
        this.mPubNub.publish(PLAY_CHANNEL, js, callback);
    }

    public void publishPauseMusic(){
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE",   "Pause Music ");
        } catch (JSONException e) { e.printStackTrace(); }

        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                Log.d("PUBNUB",response.toString());
            }
            public void errorCallback(String channel, PubnubError error) {
                Log.d("PUBNUB",error.toString());
            }
        };
        this.mPubNub.publish(PAUSE_CHANNEL, js, callback);
    }

    public void publishStopMusic() {
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE", "Stop Music ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                Log.d("PUBNUB", response.toString());
            }

            public void errorCallback(String channel, PubnubError error) {
                Log.d("PUBNUB", error.toString());
            }
        };
        this.mPubNub.publish(STOP_MUSIC_CHANNEL, js, callback);
    }


    public void publishResumeMusic() {
        JSONObject js = new JSONObject();
        try {
            js.put("MESSAGE", "Pause Music ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                Log.d("PUBNUB", response.toString());
            }

            public void errorCallback(String channel, PubnubError error) {
                Log.d("PUBNUB", error.toString());
            }
        };
        this.mPubNub.publish(RESUME_CHANNEL, js, callback);
    }

    public void subscribe(){
        try {
            this.mPubNub.subscribe(PHUE_CHANNEL, new Callback() {
                @Override
                public void connectCallback(String channel, Object message) {
                    Log.d("PUBNUB","SUBSCRIBE : CONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB","SUBSCRIBE : DISCONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                public void reconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB","SUBSCRIBE : RECONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void successCallback(String channel, Object message) {
                    Log.d("PUBNUB","SUBSCRIBE : " + channel + " : "
                            + message.getClass() + " : " + message.toString());
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    Log.d("PUBNUB", "SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());
                }
            });
            this.mPubNub.subscribe(START_CHANNEL, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : " + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    Log.d("PUBNUB", "SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());
                }

                @Override
                public void connectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : CONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void reconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : RECONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : DISCONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }
            });

            this.mPubNub.subscribe(STOP_CHANNEL, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : " + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    Log.d("PUBNUB", "SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());
                }

                @Override
                public void connectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : CONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void reconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : RECONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : DISCONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }
            });

            this.mPubNub.subscribe(PLAY_CHANNEL, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : " + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    Log.d("PUBNUB", "SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());
                }

                @Override
                public void connectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : CONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void reconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : RECONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : DISCONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }
            });

            this.mPubNub.subscribe(PAUSE_CHANNEL, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : " + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    Log.d("PUBNUB", "SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());
                }

                @Override
                public void connectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : CONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void reconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : RECONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : DISCONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }
            });

            this.mPubNub.subscribe(STOP_MUSIC_CHANNEL, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : " + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    Log.d("PUBNUB", "SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());
                }

                @Override
                public void connectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : CONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void reconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : RECONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : DISCONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }
            });

            this.mPubNub.subscribe(RESUME_CHANNEL, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : " + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    Log.d("PUBNUB", "SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());
                }

                @Override
                public void connectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : CONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void reconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : RECONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : DISCONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
