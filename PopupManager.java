package APP_PACKAGE;

import android.app.Dialog;
import java.util.ArrayList;
import android.util.Log;

/**
 * Created by nguyen.nguyendang
 * on 6/3/2019
 * 
 * Manage popups' queue
 * 
 * How to use: 
 *      -   Add ID (priority popup)
 * 
 *      -   Use it when you want to check one popup
 *          PopupManager.getInstance().checkShowPopup(PopupManager.ID, new PopupManager.PopupCallback() {
 *              @Override
 *              public void onShowPopup() {
 *                  //what you want to do
 *              }
 *          });
 * 
 * 
 *      -   Use it when dialog.dismiss();
 *          PopupManager.getInstance().checkShowQueuePopup();
 */

public class PopupManager {

    public static final int NVS_POPUP_ID = 0;
    public static final int BAN_POPUP_ID = NVS_POPUP_ID + 1;
    public static final int AGE_GATE_POPUP_ID = BAN_POPUP_ID + 1;
    public static final int FIRST_GIFT_POPUP_ID = AGE_GATE_POPUP_ID + 1;
    public static final int RATING_POPUP_ID = FIRST_GIFT_POPUP_ID + 1;
    public static final int CC_RESET_AGE_POPUP_ID = RATING_POPUP_ID + 1;
    public static final int CC_RESET_COIN_POPUP_ID = CC_RESET_AGE_POPUP_ID + 1;
    public static final int CC_GIFT_COIN_POPUP_ID = CC_RESET_COIN_POPUP_ID + 1;
    public static final int CRM_GIFT_POPUP_ID = CC_GIFT_COIN_POPUP_ID + 1;
    public static final int NUM_POPUP = CRM_GIFT_POPUP_ID + 1;

    private static PopupManager mInstance;
    private static boolean isShowing = false;

    public interface PopupCallback{
        void onShowPopup();
    }

    private PopupCallback mCallbacks[];

    ArrayList<Integer> queuePopup;

    private PopupManager(){
        queuePopup = new ArrayList<>();
        mCallbacks = new PopupCallback[NUM_POPUP];
    }

    public static PopupManager getInstance() {
        if (mInstance == null) {
            mInstance = new PopupManager();
        }
        return mInstance;
    }

    public void sortPriority() {
        int length = queuePopup.size();
        if (length <= 1) return;
        for (int i = 0; i < length - 1; i++) {
            for (int j = i+1; j < length; j++) {
                if (queuePopup.get(i) > queuePopup.get(j)) {
                    int temp = i;
                    i = j;
                    j = temp;
                }
            }
        }
    }

    public void pushPopup(int popupId) {
        queuePopup.add(popupId);
        sortPriority();
    }

    public void popPopup() {
        queuePopup.remove(0);
    }

    /**
     * use when need to show the popup
     * @param id id of popup
     * @param cb callback when popup is able to show
     */
    public void checkShowPopup(int id, PopupCallback cb) {
        setPopupCallBack(id, cb);
        pushPopup(id);
        if (!isShowing && id == queuePopup.get(0)) {
            showPopup();
        }
    }

    /**
     * use when exit a popup to show popup in the queue
     */
    public void checkShowQueuePopup() {
        isShowing = false;
        if (!queuePopup.isEmpty()) {
            showPopup();
        }
    }

    public void showPopup() {
        isShowing = true;
        int popupID = queuePopup.get(0);
        mCallbacks[popupID].onShowPopup();
        popPopup();
    }

    public void setPopupCallBack(int id, PopupCallback cb) {
        mCallbacks[id] = cb;
    }

    public static boolean isShowing() {
        return isShowing;
    }
}
