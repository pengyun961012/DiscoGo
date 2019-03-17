using UnityEngine;
using System.Collections;

public class ColumnPool : MonoBehaviour
{
    //public GameObject columnPrefab;                                 //The column game object.
    public GameObject coinPrefab;
    public int columnPoolSize = 5;                                  //How many columns to keep on standby.
    public int coinPoolSize = 25;
    public float spawnRate = 3f;                                    //How quickly columns spawn.
    public float columnMin = -1f;                                   //Minimum y value of the column position.
    public float columnMax = 3.5f;                                  //Maximum y value of the column position.

    //private GameObject[] columns;                                   //Collection of pooled columns.
    private GameObject[] coins;
    private int currentColumn = 0;                                  //Index of the current column in the collection.
    private int currentCoin = 0;

    private Vector2 objectPoolPosition = new Vector2(10f, -25f);     //A holding position for our unused columns offscreen.
    private float spawnXPosition = 20f;

    private float timeSinceLastSpawned;


    void Start()
    {
        timeSinceLastSpawned = 0f;

        //Initialize the columns collection.
        //columns = new GameObject[columnPoolSize];
        coins = new GameObject[coinPoolSize];
        //Loop through the collection... 
        for (int i = 0; i < columnPoolSize; i++)
        {
            //...and create the individual columns.
            //columns[i] = (GameObject)Instantiate(columnPrefab, objectPoolPosition, Quaternion.identity);
        }
        for(int i = 0;i < coinPoolSize; i++)
        {
            coins[i] = (GameObject)Instantiate(coinPrefab, objectPoolPosition, Quaternion.identity);
        }
    }


    //This spawns columns as long as the game is not over.
    void Update()
    {
        timeSinceLastSpawned += Time.deltaTime;

        if (GameController.instance.gameOver == false && timeSinceLastSpawned >= spawnRate)
        {
            timeSinceLastSpawned = 0f;

            //Set a random y position for the column
            float spawnYPosition = Random.Range(columnMin, columnMax);

            //...then set the current column to that position.
            //columns[currentColumn].transform.position = new Vector2(spawnXPosition, spawnYPosition);

            //Increase the value of currentColumn. If the new size is too big, set it back to zero
            currentColumn++;

            if (currentColumn >= columnPoolSize)
            {
                currentColumn = 0;
            }
            for(int i = currentCoin*10; i < currentCoin*10+10; i++)
            {
                coins[i].SetActive(true);
                coins[i].GetComponent<ScrollingObject>().enabled = true;
                coins[i].transform.position = new Vector2(spawnXPosition + 1.5f+(i-currentCoin*10) * 1f,spawnYPosition);
            }
            currentCoin++;
            if(currentCoin >= 5)
            {
                currentCoin = 0;
            }
        }
    }

}