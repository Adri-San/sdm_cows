package es.uniovi.eii.cows.controller.listener.state;

import es.uniovi.eii.cows.controller.listener.LikeClickListener;
import es.uniovi.eii.cows.data.helper.FirebaseHelper;

public class LikeState extends State{
    public LikeState(LikeClickListener listener, boolean state, int offState_icon, int onState_icon) {
        super(listener, state, offState_icon, onState_icon);
    }

    @Override
    protected void setOffState() {
        FirebaseHelper.getInstance().deleteLike(getListener().getNewsItem().getId(), l -> {
            updateState(l.getNewsItemId().toString(), getOffState_icon(), "deleted");
            return null;
        });
    }

    @Override
    protected void setOnState() {
        FirebaseHelper.getInstance().addLike(getListener().getNewsItem().getId(), l -> {
            updateState(l.getNewsItemId().toString(), getOnState_icon(),  "added");
            return null;
        });
    }

    @Override
    protected String getActionName() {
        return "Like";
    }
}
