using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class BirdForeground : MonoBehaviour
{
    // Start is called before the first frame update
    public float deltaTime;
    private bool isDead = false;
    private Rigidbody2D rb2d;
    private Animator anim;
    public bool pause = false;
    public GameObject pauseScreen;
    public GameObject pauseText;
    public GameObject pauseButton;
    public GameObject pauseResume;
    public GameObject pauseHome;
    public GameObject AudioSource;
    public AudioSource music;
    public Text CountdownText;
    public Text CoinText;
    public Text DataText;
    public float ground = -2.3f;
    public float sky = 4.7f;
    private int pitch = 350;
    public int coins = 0;
    public readonly int MAXIMUM = 600;
    public readonly int MINIMUM = 100;
    void Start()
    {
        pauseScreen.SetActive(false);
        pauseText.SetActive(false);
        pauseHome.SetActive(false);
        pauseResume.SetActive(false);
        rb2d = GetComponent<Rigidbody2D>();
        anim = GetComponent<Animator>();
        music = AudioSource.GetComponent<AudioSource>();
    }

    // Update is called once per frame
    void Update()
    {
        if (!pause && !isDead)
        {
            if (pitch > MAXIMUM) pitch = MAXIMUM;
            else if (pitch < MINIMUM) pitch = MINIMUM;
            float target = ground + (pitch - MINIMUM) / ((MAXIMUM-MINIMUM) / 7f);
            float current = rb2d.transform.position.y;
            if(target != current)
            {
                rb2d.velocity = new Vector2(0, (target - current) * 20f);
                //rb2d.AddForce(new Vector2(0, (target-current)*5f),ForceMode2D.Force);
                anim.SetTrigger("Flap");
            }
        }
    }
    
    public void unPause(){
        pause = !pause;
        StartCoroutine(getReady());
    }

    void OnCollisionEnter2D()
    {
        Debug.Log("Collide!");
        rb2d.angularVelocity = 0f;
        rb2d.velocity = Vector2.zero;
        isDead = true;
        anim.SetTrigger("Die");
        GameController.instance.BirdDied();
        GameOverScreen.instance.appearWindow();
    }

    public void Pause()
    {
        pause = !pause;
        if (pause)
        {
            music.Pause();
            Time.timeScale = 0;
            pauseScreen.SetActive(true);
            pauseText.SetActive(true);
            pauseButton.SetActive(false);
            pauseHome.SetActive(true);
            pauseResume.SetActive(true);

        }
    }
    private IEnumerator WaitForIt()
    {
        WaitForSeconds wait = new WaitForSeconds(3f);
        yield return wait;
    }
    IEnumerator getReady()
    {
        //rb2d.velocity = Vector2.zero;
        pauseScreen.SetActive(false);
        pauseText.SetActive(false);
        pauseHome.SetActive(false);
        pauseResume.SetActive(false);
        CountdownText.text = "3";
        yield return StartCoroutine(WaitForRealSeconds(1f));
        CountdownText.text = "2";
        yield return StartCoroutine(WaitForRealSeconds(1f));
        CountdownText.text = "1";
        yield return StartCoroutine(WaitForRealSeconds(1f));
        CountdownText.text = "";
        pauseButton.SetActive(true);
        music.UnPause();
        Time.timeScale = 1f;
    }

    IEnumerator WaitForRealSeconds(float waitTime)
    {
        float endTime = Time.realtimeSinceStartup + waitTime;

        while (Time.realtimeSinceStartup < endTime)
        {
            yield return null;
        }
    }

    public void receiveData(string data)
    {
        pitch = Int32.Parse(data);
        DataText.text = pitch.ToString();
    }

    public void OnTriggerEnter2D(Collider2D other)
    {
        Debug.Log("Trigger Collision");
        if (other.gameObject.CompareTag("PickUp"))
        {
            other.gameObject.SetActive(false);
            coins++;
            CoinText.text = coins.ToString();
        }
        else if (other.gameObject.CompareTag("Sky"))
        {
            return;
        }
    }
}
