package es.uniovi.eii.cows.controller.listener.state;

import es.uniovi.eii.cows.controller.listener.SaveClickListener;
import es.uniovi.eii.cows.data.helper.FirebaseHelper;

public class SaveState extends State{
    public SaveState(SaveClickListener listener, boolean state, int offState_image, int onState_image) {
        super(listener, state, offState_image, onState_image);
    }

    @Override
    protected void setOffState() {
        FirebaseHelper.getInstance().deleteSave(getListener().getNewsItem().getId(), s -> {
            updateState(s.getNewsItemId().toString(), getOffState_icon(), "deleted");
            return null;
        });
    }

    @Override
    protected void setOnState() {
        FirebaseHelper.getInstance().addSave(getListener().getNewsItem().getId(), s -> {
            updateState(s.getNewsItemId().toString(), getOnState_icon(), "added");
            return null;
        });
    }

    @Override
    protected String getActionName() {
        return "Save";
    }
}
