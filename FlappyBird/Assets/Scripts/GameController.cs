using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
using System;
using Melanchall.DryWetMidi.Smf;
using Melanchall.DryWetMidi.Smf.Interaction;
using System.Collections.Specialized;
using System.Diagnostics;

public class GameController : MonoBehaviour
{
    public static GameController instance;         //A reference to our game control script so we can access it statically.
    public Text scoreText;                      //A reference to the UI text component that displays the player's score.
    public Text displayScore;
    public GameObject pauseButton;
    public GameObject scoreIcon;
    public GameObject coinIcon;
    public GameObject CoinText;
    public Dictionary<int, double> frequency_Table;
    public List<long> time;
    public List<float> frequency;
    public long[] keys;
    public float[] values;
    //public GameObject gameOvertext;             //A reference to the object that displays the text which appears when the player dies.

    public int score = 0;                      //The player's score.
    public bool gameOver = false;               //Is the game over?
    public float scrollSpeed = -1.5f;


    void Awake()
    {
        frequency_Table = new Dictionary<int, double>();
        frequency_Table.Add(21, 27.500);
        frequency_Table.Add(22, 29.135);
        frequency_Table.Add(23, 30.868);
        frequency_Table.Add(24, 32.703);
        frequency_Table.Add(25, 34.648);
        frequency_Table.Add(26, 36.708);
        frequency_Table.Add(27, 38.891);
        frequency_Table.Add(28, 41.203);
        frequency_Table.Add(29, 43.654);
        frequency_Table.Add(30, 46.249);
        frequency_Table.Add(31, 48.999);
        frequency_Table.Add(32, 51.913);
        frequency_Table.Add(33, 55.000);
        frequency_Table.Add(34, 58.270);
        frequency_Table.Add(35, 61.735);
        frequency_Table.Add(36, 65.406);
        frequency_Table.Add(37, 69.296);
        frequency_Table.Add(38, 73.416);
        frequency_Table.Add(39, 77.782);
        frequency_Table.Add(40, 82.407);
        frequency_Table.Add(41, 87.307);
        frequency_Table.Add(42, 92.499);
        frequency_Table.Add(43, 97.999);
        frequency_Table.Add(44, 103.83);
        frequency_Table.Add(45, 110.00);
        frequency_Table.Add(46, 116.54);
        frequency_Table.Add(47, 123.47);
        frequency_Table.Add(48, 130.81);
        frequency_Table.Add(50, 146.83);
        frequency_Table.Add(51, 155.56);
        frequency_Table.Add(52, 164.81);
        frequency_Table.Add(53, 174.61);
        frequency_Table.Add(54, 185.00);
        frequency_Table.Add(49, 138.59);
        frequency_Table.Add(55, 196.00);
        frequency_Table.Add(56, 207.65);
        frequency_Table.Add(57, 220.00);
        frequency_Table.Add(58, 233.08);
        frequency_Table.Add(59, 246.94);
        frequency_Table.Add(60, 261.63);
        frequency_Table.Add(61, 277.18);
        frequency_Table.Add(62, 293.67);
        frequency_Table.Add(63, 311.13);
        frequency_Table.Add(64, 329.63);
        frequency_Table.Add(65, 349.23);
        frequency_Table.Add(66, 369.99);
        frequency_Table.Add(67, 392.00);
        frequency_Table.Add(68, 415.30);
        frequency_Table.Add(69, 440.00);
        frequency_Table.Add(70, 466.16);
        frequency_Table.Add(71, 493.88);
        frequency_Table.Add(72, 523.25);
        frequency_Table.Add(73, 554.37);
        frequency_Table.Add(74, 587.33);
        frequency_Table.Add(75, 622.25);
        frequency_Table.Add(76, 659.26);
        frequency_Table.Add(77, 698.46);
        //If we don't currently have a game control...
        if (instance == null)
            //...set this one to be it...
            instance = this;
        //...otherwise...
        else if (instance != this)
            //...destroy this one because it is a duplicate.
            Destroy(gameObject);
        time.Add(0);
        frequency.Add(261.63f);
        time.Add(480);
        frequency.Add(261.63f);
        time.Add(960);
        frequency.Add(392f);
        time.Add(1440);
        frequency.Add(392f);
        time.Add(1920);
        frequency.Add(440f);
        time.Add(2400);
        frequency.Add(440f);
        time.Add(2880);
        frequency.Add(392f);
        time.Add(3840);
        frequency.Add(349.23f);
        time.Add(4320);
        frequency.Add(349.23f);
        time.Add(4800);
        frequency.Add(329.63f);
        time.Add(5280);
        frequency.Add(329.63f);
        time.Add(5760);
        frequency.Add(293.67f);
        time.Add(6240);
        frequency.Add(293.67f);
        time.Add(6720);
        frequency.Add(261.63f);
        time.Add(7680);
        frequency.Add(392f);
        time.Add(8160);
        frequency.Add(392f);
        time.Add(8640);
        frequency.Add(349.23f);
        time.Add(9120);
        frequency.Add(349.23f);
        time.Add(9600);
        frequency.Add(329.63f);
        time.Add(10080);
        frequency.Add(329.63f);
        time.Add(10560);
        frequency.Add(293.67f);
        time.Add(11520);
        frequency.Add(392f);
        time.Add(12000);
        frequency.Add(392f);
        time.Add(12480);
        frequency.Add(349.23f);
        time.Add(12960);
        frequency.Add(349.23f);
        time.Add(13440);
        frequency.Add(329.63f);
        time.Add(13920);
        frequency.Add(329.63f);
        time.Add(14400);
        frequency.Add(293.67f);
        time.Add(15360);
        frequency.Add(261.63f);
        time.Add(16080);
        frequency.Add(261.63f);
        time.Add(16320);
        frequency.Add(392f);
        time.Add(17040);
        frequency.Add(392f);
        time.Add(17280);
        frequency.Add(440f);
        time.Add(18000);
        frequency.Add(440f);
        time.Add(18240);
        frequency.Add(440f);
        time.Add(18720);
        frequency.Add(392f);
        time.Add(19200);
        frequency.Add(349.23f);
        time.Add(19920);
        frequency.Add(349.23f);
        time.Add(20160);
        frequency.Add(329.63f);
        time.Add(20880);
        frequency.Add(329.63f);
        time.Add(21120);
        frequency.Add(293.67f);
        time.Add(21360);
        frequency.Add(261.63f);
        time.Add(21600);
        frequency.Add(293.67f);
        time.Add(21840);
        frequency.Add(329.63f);
        time.Add(22080);
        frequency.Add(261.63f);
        time.Add(23040);
        frequency.Add(261.63f);
        time.Add(23280);
        frequency.Add(293.67f);
        time.Add(23520);
        frequency.Add(261.63f);
        time.Add(23760);
        frequency.Add(293.67f);
        time.Add(24000);
        frequency.Add(392f);
        time.Add(24240);
        frequency.Add(392f);
        time.Add(24480);
        frequency.Add(392f);
        time.Add(24720);
        frequency.Add(392f);
        time.Add(24960);
        frequency.Add(440f);
        time.Add(25200);
        frequency.Add(440f);
        time.Add(25440);
        frequency.Add(440f);
        time.Add(25680);
        frequency.Add(440f);
        time.Add(25920);
        frequency.Add(392f);
        time.Add(26880);
        frequency.Add(349.23f);
        time.Add(27120);
        frequency.Add(349.23f);
        time.Add(27360);
        frequency.Add(349.23f);
        time.Add(27600);
        frequency.Add(349.23f);
        time.Add(27840);
        frequency.Add(329.63f);
        time.Add(28320);
        frequency.Add(349.23f);
        time.Add(28800);
        frequency.Add(293.67f);
        time.Add(29040);
        frequency.Add(293.67f);
        time.Add(29280);
        frequency.Add(293.67f);
        time.Add(29520);
        frequency.Add(293.67f);
        time.Add(29760);
        frequency.Add(261.63f);
        time.Add(30720);
        frequency.Add(392f);
        time.Add(30960);
        frequency.Add(392f);
        time.Add(31200);
        frequency.Add(392f);
        time.Add(31440);
        frequency.Add(392f);
        time.Add(31680);
        frequency.Add(349.23f);
        time.Add(32160);
        frequency.Add(349.23f);
        time.Add(32400);
        frequency.Add(329.63f);
        time.Add(32640);
        frequency.Add(329.63f);
        time.Add(32880);
        frequency.Add(329.63f);
        time.Add(33120);
        frequency.Add(329.63f);
        time.Add(33360);
        frequency.Add(329.63f);
        time.Add(33600);
        frequency.Add(293.67f);
        time.Add(34080);
        frequency.Add(293.67f);
        time.Add(34560);
        frequency.Add(392f);
        time.Add(34800);
        frequency.Add(392f);
        time.Add(35040);
        frequency.Add(392f);
        time.Add(35280);
        frequency.Add(392f);
        time.Add(35520);
        frequency.Add(349.23f);
        time.Add(35640);
        frequency.Add(349.23f);
        time.Add(35760);
        frequency.Add(349.23f);
        time.Add(35880);
        frequency.Add(349.23f);
        time.Add(36000);
        frequency.Add(349.23f);
        time.Add(36120);
        frequency.Add(349.23f);
        time.Add(36240);
        frequency.Add(349.23f);
        time.Add(36360);
        frequency.Add(349.23f);
        time.Add(36480);
        frequency.Add(329.63f);
        time.Add(36720);
        frequency.Add(329.63f);
        time.Add(36960);
        frequency.Add(329.63f);
        time.Add(37200);
        frequency.Add(293.67f);
        time.Add(37440);
        frequency.Add(293.67f);
        time.Add(37920);
        frequency.Add(293.67f);
    }

    void Update()
    {
    }

    public void BirdScored()
    {
        //The bird can't score if the game is over.
        if (gameOver)
            return;
        //If the game is not over, increase the score...
        score++;
        //...and adjust the score text.
        scoreText.text = score.ToString();
        displayScore.text = score.ToString();
    }

    public void BirdDied()
    {
        //Activate the game over text.
        //gameOvertext.SetActive(true);
        scoreText.enabled = false;
        pauseButton.SetActive(false);
        scoreIcon.SetActive(false);
        coinIcon.SetActive(false);
        CoinText.SetActive(false);
        //Set the game to be over.
        gameOver = true;
    }
}
