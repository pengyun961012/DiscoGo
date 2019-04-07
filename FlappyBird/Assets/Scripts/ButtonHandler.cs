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
        Application.Quit();
        CallAndroidMethod("stopRecorder",GameController.instance.score, BirdForeground.instance.coins);
    }
    public static void CallAndroidMethod(string methodName, int score, int coins)
    {
        using (var clsUnityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer"))
        {
            using (var objActivity = clsUnityPlayer.GetStatic<AndroidJavaObject>("currentActivity"))
            {
                objActivity.Call(methodName, score, coins);
            }
        }
    }
}
