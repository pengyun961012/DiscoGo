using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameClearScreen : MonoBehaviour
{
    // Start is called before the first frame update
    Animator anim;
    public static GameClearScreen instance;
    void Awake()
    {
        if (instance == null)
        {
            anim = GetComponent<Animator>();
            instance = this;
        }
        else
        {
            Destroy(gameObject);
        }

    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void appearWindow()
    {
        Debug.Log("Start Playing!");
        anim.SetTrigger("GameOver");
    }
}
