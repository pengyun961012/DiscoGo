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
    
    public void proceedtoNextStage()
    {
        GameController.level++;
        CallAndroidMethod2("levelUp", GameController.level);
    }

    public void mainPage()
    {
        CallAndroidMethod("stopRecorder",GameController.instance.score, BirdForeground.instance.coins);
        Application.Quit();
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

    public static void CallAndroidMethod2(string methodName, int level)
    {
        using (var clsUnityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer"))
        {
            using (var objActivity = clsUnityPlayer.GetStatic<AndroidJavaObject>("currentActivity"))
            {
                objActivity.Call(methodName, level);
            }
        }
    }
}
