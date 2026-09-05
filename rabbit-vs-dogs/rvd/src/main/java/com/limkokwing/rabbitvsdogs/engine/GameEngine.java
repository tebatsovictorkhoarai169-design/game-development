package com.limkokwing.rabbitvsdogs.engine;

import com.limkokwing.rabbitvsdogs.model.BoardGraph;
import java.util.*;

public class GameEngine
{
    public enum Turn { RABBIT, DOGS }
    public enum Outcome { NONE, DOGS_WIN, RABBIT_WIN_TIMEOUT, RABBIT_WIN_DOGS_STUCK }

    private int rabbitPos;
    private final int[] dogPos = new int[3];
    private Turn turn = Turn.RABBIT;
    private Outcome outcome = Outcome.NONE;
    private int rabbitMoveCount = 0;

    public GameEngine()
    {
        reset();
    }

    public void reset()
    {
        rabbitPos = 0;
        rabbitMoveCount = 0;
        Random rnd = new Random();
        Set<Integer> chosen = new LinkedHashSet<>();
        List<Integer> pool = new ArrayList<>();

        for (int n = 1; n <= 10; n++) pool.add(n);

        while (chosen.size() < 3)
        {
            int idx = rnd.nextInt(pool.size());
            chosen.add(pool.get(idx));
        }

        int i = 0;
        for (int p : chosen) dogPos[i++] = p;
        turn = Turn.RABBIT;
        outcome = Outcome.NONE;
    }

    public int getRabbitPos()
    {
        return rabbitPos;
    }
    public int getDogPos(int dogIndex)
    {
        return dogPos[dogIndex];
    }
    public int[] getDogPositions()
    {
        return dogPos.clone();
    }
    public Turn getTurn()
    {
        return turn;
    }
    public Outcome getOutcome()
    {
        return outcome;
    }
    public boolean isGameOver()
    {
        return outcome != Outcome.NONE;
    }
    public int getRabbitMoveCount()
    {
        return rabbitMoveCount;
    }

    public boolean isOccupied(int node)
    {
        if (node == rabbitPos) return true;
        for (int d : dogPos) if (d == node) return true;
        return false;
    }

    public int dogAt(int node)
    {
        for (int i = 0; i < dogPos.length; i++) if (dogPos[i] == node) return i;
        return -1;
    }

    public List<Integer> legalMovesFrom(int from)
    {
        List<Integer> result = new ArrayList<>();

        for (int n : BoardGraph.neighbours(from))
        {
            if (!isOccupied(n)) result.add(n);
        }
        return result;
    }

    public boolean rabbitIsTrapped()
    {
        return legalMovesFrom(rabbitPos).isEmpty();
    }

    public boolean anyDogHasAMove()
    {
        for (int d : dogPos) if (!legalMovesFrom(d).isEmpty()) return true;
        return false;
    }

    public boolean moveRabbit(int to)
    {
        if (isGameOver() || turn != Turn.RABBIT) return false;
        if (!legalMovesFrom(rabbitPos).contains(to)) return false;
        rabbitPos = to;
        rabbitMoveCount++;
        turn = Turn.DOGS;
        checkPostMoveConditions();
        return true;
    }

    public boolean moveDog(int dogIndex, int to)
    {
        if (isGameOver() || turn != Turn.DOGS) return false;
        if (!legalMovesFrom(dogPos[dogIndex]).contains(to)) return false;
        dogPos[dogIndex] = to;
        turn = Turn.RABBIT;
        checkPostMoveConditions();
        return true;
    }

    public void onTimerTick(int secondsRemaining)
    {
        if (isGameOver())
        {
            return;
        }

        if (secondsRemaining <= 0 && !rabbitIsTrapped())
        {
            outcome = Outcome.RABBIT_WIN_TIMEOUT;
        }
    }

    private void checkPostMoveConditions()
    {
        if (rabbitIsTrapped())
        {
            outcome = Outcome.DOGS_WIN; return;
        }

        if (turn == Turn.DOGS && !anyDogHasAMove())
        {
            outcome = Outcome.RABBIT_WIN_DOGS_STUCK;
        }
    }
}