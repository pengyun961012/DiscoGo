using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class ButtonHandler : MonoBehaviour
{
    // Start is called before the first frame update
    public void replayGame()
    {
        SceneManager.LoadScene(SceneManager.GetActiveScene().buildIndex);
    }

    public void mainPage()
    {
        CallAndroidMethod("stopRecorder");
        Application.Quit();

    }
    public static void CallAndroidMethod(string methodName)
    {
        using (var clsUnityPlayer = new AndroidJavaClass("com.unity2d.player.UnityPlayer"))
        {
            using (var objActivity = clsUnityPlayer.GetStatic<AndroidJavaObject>("currentActivity"))
            {
                objActivity.Call(methodName);
            }
        }
    }
}
