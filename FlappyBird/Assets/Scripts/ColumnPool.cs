using UnityEngine;
using System.Collections;

public class ColumnPool : MonoBehaviour
{
    //public GameObject columnPrefab;                                 //The column game object.
    public GameObject coinPrefab;
    public int columnPoolSize = 5;                                  //How many columns to keep on standby.
    public int coinPoolSize = 150;
    //public float spawnRate = 3f;                                    //How quickly columns spawn.
    public float columnMin = -1f;                                   //Minimum y value of the column position.
    public float columnMax = 3.5f;                                  //Maximum y value of the column position.
    //private GameObject[] columns;                                   //Collection of pooled columns.
    private GameObject[] coins;
    private int currentColumn = 0;                                  //Index of the current column in the collection.
    private int currentCoin = 0;
    public int counter = 0;
    public int max_last_index;

    private Vector2 objectPoolPosition = new Vector2(10f, -25f);     //A holding position for our unused columns offscreen.
    private float spawnXPosition = 0;

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
        while (spawnXPosition <= 18f)
        {
            coins[counter].transform.position = new Vector2(GameController.instance.time[counter]*GameController.instance.scrollSpeed*(-1)*0.001f,-2.3f+(GameController.instance.frequency[counter]-50f)*7f/600);
            counter++;
            spawnXPosition = GameController.instance.time[counter-1] * GameController.instance.scrollSpeed * (-1) * 0.001f;
        }
        currentCoin = counter;
        max_last_index = counter - 1;
    }


    //This spawns columns as long as the game is not over.
    void Update()
    {
        timeSinceLastSpawned += Time.deltaTime;

        if (GameController.instance.gameOver == false && counter < GameController.instance.time.Count && timeSinceLastSpawned >= (GameController.instance.time[counter]-GameController.instance.time[max_last_index])*0.001f)
        {

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
            coins[currentCoin].SetActive(true);
            coins[currentCoin].GetComponent<ScrollingObject>().enabled = true;
            coins[currentCoin].transform.position = new Vector2(spawnXPosition + (GameController.instance.time[counter]-GameController.instance.time[counter-1])*1.5f*0.001f, -2.3f+(GameController.instance.frequency[counter] - 50f) * 7f / 600);
            currentCoin++;
            counter++;
            if(currentCoin >= coinPoolSize)
            {
                currentCoin = 0;
            }
        }
    }

}