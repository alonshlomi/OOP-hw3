# The Maze of Waze

This project is an implemention of a basic game based on previous assignment.  
In this game we play with _Robots_ collecting _Fruits_ on a weighted graph.  
The goal of the game is to earn as many points as possible in given time. (30-60 seconds)  
The game has 24 levels, starting in 0 and the level of difficulty increases, each level can be exported to KML file.  
The game can be play in 2 modes: _Manual_(by user) , _Auto_(by computer)  

_Addition for Ex4_:  
You can watch your own stats and other players' stats using MySQL database.

The project is made out 3 packages taken from previous assignment - [Ex2](https://github.com/alonshlomi/OOP-hw2)   
* **algorithms** 
* **dataStructure**
* **utils**

The package we programmed in this game is **gameClient** contains 8 classes:
* _Fruit_
* _Robot_
* _GameArena_
* _AutoGame_
* _ClientThread_
* _MyGameGUI_
* _KML_Logger_
* _ServerDB_ (added in Ex4)

(More details about the classes can be found on Wiki)

#### How to play The Maze of Waze:  
* Launch the game and enter your ID:  (_Added on Ex4_)  
![id](https://github.com/alonshlomi/OOP-hw3/blob/master/images/id.png)

* Choose scenario: (0-23)  
![scenario](https://github.com/alonshlomi/OOP-hw3/blob/master/images/scenario.png)

* Choose playing mode: (_Auto_ or _Manual_)   
![mode](https://github.com/alonshlomi/OOP-hw3/blob/master/images/mode.png)

* Start playing:  
![example](https://github.com/alonshlomi/OOP-hw3/blob/master/images/example.png)

* Click on any robot to choose its next node: (in manual mode)  
![robot](https://github.com/alonshlomi/OOP-hw3/blob/master/images/robot.png)

* Choose whether you want to export KML file or not:  
![kml](https://github.com/alonshlomi/OOP-hw3/blob/master/images/kml.png)  

* The score you earnd will be shown:  
![score](https://github.com/alonshlomi/OOP-hw3/blob/master/images/score.png)

#### Watch stats: 
* Click _Stats_ on menu:  
![stats](https://github.com/alonshlomi/OOP-hw3/blob/master/images/stats.png)  

* Click _My Stats_ to see your stats:  
![mystats](https://github.com/alonshlomi/OOP-hw3/blob/master/images/mystats.png)  

* Click _Class Stats to see the class stats:  
![classstats](https://github.com/alonshlomi/OOP-hw3/blob/master/images/classstats.png)  

