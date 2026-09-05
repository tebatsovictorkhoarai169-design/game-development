package com.limkokwing.rabbitvsdogs.model;

import java.util.*;

public class GameState
{
    private int rabbitPosition;
    private final int[] dogPositions = new int[3];

    private PieceType turn = PieceType.RABBIT;
    private final Difficulty difficulty;
    private int secondsRemaining;

    private boolean gameOver = false;
    private PieceType winner = null;
    private int moveCount = 0;

    public GameState(Difficulty difficulty)
    {
        this.difficulty = difficulty;
        this.secondsRemaining = difficulty.getSeconds();
        randomiseStartingPositions();
    }

    // Rabbit always starts at node 0; the three dogs get distinct random nodes 1-10
    private void randomiseStartingPositions()
    {
        rabbitPosition = 0;
        List<Integer> pool = new ArrayList<>();
        for (int i = 1; i < BoardGraph.NODE_COUNT; i++)
        {
            pool.add(i);
        }
        Collections.shuffle(pool);
        dogPositions[0] = pool.get(0);
        dogPositions[1] = pool.get(1);
        dogPositions[2] = pool.get(2);
    }

    public int getRabbitPosition()
    {
        return rabbitPosition;
    }

    public void setRabbitPosition(int node)
    {
        this.rabbitPosition = node;
    }

    public int[] getDogPositions()
    {
        return dogPositions;
    }

    public int getDogPosition(int dogIndex)
    {
        return dogPositions[dogIndex];
    }

    public void setDogPosition(int dogIndex, int node)
    {
        dogPositions[dogIndex] = node;
    }

    // Every node currently occupied by any piece
    public Set<Integer> occupiedNodes()
    {
        Set<Integer> occupied = new HashSet<>();
        occupied.add(rabbitPosition);

        for (int d : dogPositions)
        {
            occupied.add(d);
        }
        return occupied;
    }

    // Every node currently occupied by a dog
    public Set<Integer> dogOccupiedNodes()
    {
        Set<Integer> occupied = new HashSet<>();
        for (int d : dogPositions)
        {
            occupied.add(d);
        }
        return occupied;
    }

    // -1 if no dog is on that node, otherwise the dog's index (0-2)
    public int dogIndexAt(int node)
    {
        for (int i = 0; i < dogPositions.length; i++)
        {
            if (dogPositions[i] == node)
            {
                return i;
            }
        }
        return -1;
    }

    public PieceType getTurn()
    {
        return turn;
    }

    public void setTurn(PieceType turn)
    {
        this.turn = turn;
    }

    public void toggleTurn()
    {
        turn = (turn == PieceType.RABBIT) ? PieceType.DOG : PieceType.RABBIT;
    }

    public Difficulty getDifficulty()
    {
        return difficulty;
    }

    public int getSecondsRemaining()
    {
        return secondsRemaining;
    }

    public void setSecondsRemaining(int secondsRemaining)
    {
        this.secondsRemaining = Math.max(0, secondsRemaining);
    }

    public void decrementSecond()
    {
        if (secondsRemaining > 0)
        {
            secondsRemaining--;
        }
    }

    public boolean isGameOver()
    {
        return gameOver;
    }

    public PieceType getWinner()
    {
        return winner;
    }

    public void endGame(PieceType winner)
    {
        this.gameOver = true;
        this.winner = winner;
    }

    public int getMoveCount()
    {
        return moveCount;
    }

    public void incrementMoveCount()
    {
        moveCount++;
    }

    //mins:secs e.g. "04:35"
    public String formattedTime()
    {
        int m = secondsRemaining / 60;
        int s = secondsRemaining % 60;

        return String.format("%02d:%02d", m, s);
    }
}
