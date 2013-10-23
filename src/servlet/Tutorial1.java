package servlet;

import sim.engine.*;
import sim.field.grid.*;

public class Tutorial1 extends SimState
    {
    private static final long serialVersionUID = 1;

    public Tutorial1(long seed)
        {
        super(seed);
        }
    
    // our own parameters for setting the grid size later on
    public IntGrid2D grid;
    
    public int gridWidth = 100;
    public int gridHeight = 100;
    
    // A b-heptomino looks like this:
    //  X
    // XXX
    // X XX
    public static final int[][] b_heptomino = new int[][]
    {{0, 1, 1},
         {1, 1, 0},
         {0, 1, 1},
         {0, 0, 1}};
    
    void seedGrid()
        {
        // we stick a b_heptomino in the center of the grid
        for(int x=0;x<b_heptomino.length;x++)
            for(int y=0;y<b_heptomino[x].length;y++)
                grid.field[x + grid.field.length/2 - b_heptomino.length/2]
                    [y + grid.field[x].length/2 - b_heptomino[x].length/2] =
                    b_heptomino[x][y];
        }
    
    public int numberOfDead(){
    	int dead = 0;
    	for(int i = 0; i < grid.getWidth(); i++){
    		for(int j = 0; j < grid.getHeight(); j++){
    			if(grid.get(i, j) == 0){
    				dead++;
    			}
    		}
    	}
    	return dead;
    }
    
    public int numberOfAlive(){
    	int alive = 0;
    	for(int i = 0; i < grid.getWidth(); i++){
    		for(int j = 0; j < grid.getHeight(); j++){
    			if(grid.get(i, j) == 1){
    				alive++;
    			}
    		}
    	}
    	return alive;
    }
    
    public void setGridDimension(int gridW, int gridH){
    	grid = new IntGrid2D(gridW, gridH);
    }
    
    public void start()
        {
        super.start();
        seedGrid();
        schedule.scheduleRepeating(new CA());
        }

    }