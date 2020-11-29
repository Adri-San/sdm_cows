package es.uniovi.eii.cows.controller.listener.state;

import android.util.Log;

import com.google.android.material.button.MaterialButton;

import es.uniovi.eii.cows.controller.listener.OnButtonClickListener;

public abstract class State {
    // Button icons
    private final int[] state_icons;

    // Button clicked state
    private boolean isOn;

    // Reference
    private final OnButtonClickListener listener;

    public State(OnButtonClickListener listener, boolean state, int offState_icon, int onState_icon) {
        this.listener = listener;
        this.isOn = state;
        this.state_icons = new int[]{offState_icon, onState_icon};
    }

    public void switchState(){
        isOn = !isOn;
    }

    public int getOffState_icon() {
        return state_icons[0];
    }
    public int getOnState_icon() {
        return state_icons[1];
    }

    public OnButtonClickListener getListener() {
        return listener;
    }

    protected abstract void setOffState();
    protected abstract void setOnState();
    protected abstract String getActionName();

    public void changeState(){
        if (isOn) setOffState();
        else setOnState();
    }

    /**
     * Switch state and icon for the button
     * @param id, news item id
     * @param state_icon, the new icon for the button
     * @param message showing the action
     */
    public void updateState(String id, int state_icon, String message) {
        if (id != null) {
            switchState();
            ((MaterialButton) listener.getButton()).setIconResource(state_icon);
            Log.d(getActionName() + " " +  message, id);
        } else
            Log.d(getActionName() + " not " +  message + ":", "Failure in Repository");
    }
}
