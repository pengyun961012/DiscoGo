using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameOverScreen : MonoBehaviour
{
    // Start is called before the first frame update
    Animator anim;
    public static GameOverScreen instance;
    void Awake()
    {
        if(instance == null)
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
        anim.SetTrigger("GameOver");
    }
    
}
