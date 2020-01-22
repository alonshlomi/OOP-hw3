package gameClient;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.node_data;

import java.util.Hashtable;
import java.util.List;

public class AutoGame {


    private GameArena arena;
    private Graph_Algo algo_g;
    private Hashtable<Integer, List<node_data>> robots_paths;

    public AutoGame(GameArena arena) {
        this.arena = arena;
        robots_paths = new Hashtable<>();
        algo_g = new Graph_Algo(arena.getGraph());
        initRobotPath();
    }

    public void moveRobots(game_service game)
    {
        int dest = -1;
        for (int i = 0; i < arena.numOfRobots(); i++) {
            Robot robot = arena.getRobots().get(i);
            if (robot.getDest() == -1) {
                dest = nextNode(i);
                game.chooseNextEdge(i, dest);
                }
            }
        }

    private int nextNode(int rid) {

        Robot robot = arena.getRobots().get(rid);
        List<node_data> tmp = robots_paths.get(rid);
        if (tmp.isEmpty()) {
            synchronized (arena.getFruits()) {
                if (arena.getFruits().size() > 0) {
                    Fruit fruit = findClosestFruit(robot);
                    tmp = algo_g.shortestPath(robot.getSrc(),fruit.getEdge().getSrc());
                    node_data dest = arena.getGraph().getNode(fruit.getEdge().getDest());
                    tmp.add(dest);
                    robots_paths.put(robot.getID(),tmp);
                }
            }
        }

        node_data n = tmp.get(0);
        tmp.remove(0);
        return n.getKey();
//        for (int i = 0; i < tmp.size(); i++) {
//            node_data n = tmp.get(i);
//            tmp.remove(i);
//            if (n.getKey() == robot.getSrc())
//                continue;
//            return n.getKey();
//        }
//        return  -1;
    }

    private Fruit findClosestFruit(Robot robot) {
        double min_dist = Double.MAX_VALUE;
        Fruit ans = null;
        for(int i = 0; i < arena.getFruits().size() ; i ++) {
            Fruit fruit = arena.getFruits().get(i);
     //       if(fruit.isTargeted()) continue;
            double dist = algo_g.shortestPathDist(robot.getSrc(),fruit.getEdge().getSrc());
            if(dist < min_dist) {
                min_dist = dist;
                ans = fruit;
            }
        }
       // ans.setTargeted(true);
        return ans;
    }

    public void initRobotPath() {
        for (int i = 0; i < arena.numOfRobots(); i++) {
            Robot robot = arena.getRobots().get(i);
            Fruit fruit = arena.getFruits().get(i);
            List<node_data> tmp = algo_g.shortestPath(robot.getSrc(), fruit.getEdge().getDest());
            robots_paths.put(robot.getID(), tmp);
        }
    }
}