package com.codenjoy.dojo.snakebattle.ai;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.client.Board;
import com.codenjoy.dojo.snakebattle.model.Elements;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.codenjoy.dojo.services.Direction.DOWN;
import static com.codenjoy.dojo.services.Direction.LEFT;
import static com.codenjoy.dojo.services.Direction.RIGHT;
import static com.codenjoy.dojo.services.Direction.UP;
import static com.codenjoy.dojo.snakebattle.model.Elements.BODY_HORIZONTAL;
import static com.codenjoy.dojo.snakebattle.model.Elements.BODY_LEFT_DOWN;
import static com.codenjoy.dojo.snakebattle.model.Elements.BODY_LEFT_UP;
import static com.codenjoy.dojo.snakebattle.model.Elements.BODY_RIGHT_DOWN;
import static com.codenjoy.dojo.snakebattle.model.Elements.BODY_RIGHT_UP;
import static com.codenjoy.dojo.snakebattle.model.Elements.BODY_VERTICAL;
import static com.codenjoy.dojo.snakebattle.model.Elements.TAIL_END_LEFT;
import static com.codenjoy.dojo.snakebattle.model.Elements.TAIL_END_RIGHT;
import static com.codenjoy.dojo.snakebattle.model.Elements.TAIL_END_UP;
import static com.codenjoy.dojo.snakebattle.model.Elements.TAIL_INACTIVE;

public class SnakeAi {

    private Direction dir = RIGHT;

    private static final Set<Elements> BARRIER_ELEMS = Set.of(Elements.TAIL_END_DOWN, TAIL_END_LEFT, TAIL_END_UP, TAIL_END_RIGHT,
            TAIL_INACTIVE, BODY_HORIZONTAL, BODY_VERTICAL, BODY_LEFT_DOWN, BODY_LEFT_UP, BODY_RIGHT_DOWN, BODY_RIGHT_UP);

    public Direction move(Board board) {
        Point head = board.getMe();

        int headX = head.getX();
        int headY = head.getY();

        System.out.println("Head: [" + headX + ", " + headY + "]");

        Elements element = board.getAt(headX, headY);
        switch(element) {
            case HEAD_UP:
                dir = UP;
                break;
            case HEAD_DOWN:
                dir = DOWN;
                break;
            case HEAD_LEFT:
                dir = LEFT;
                break;
            case HEAD_RIGHT:
                dir = RIGHT;
                break;
        }

        List<Point> targets = new ArrayList<>();

        targets.addAll(board.get(Elements.GOLD));
        targets.addAll(board.get(Elements.APPLE));
        // targets.addAll(board.get(Elements.FLYING_PILL));
        targets.addAll(board.get(Elements.FURY_PILL));

        int distance = Integer.MAX_VALUE;
        Point target = null;
        for (Point apple : targets) {
            if ((apple.getX() == 9 || apple.getX() == 10) && (apple.getY() == 20) || apple.getY() == 21) {
                continue;
            }
            int manhattan = manhattan(headX, headY, apple.getX(), apple.getY());
            if (manhattan < distance) {
                target = apple;
                distance = manhattan;
            }
        }

        System.out.println("Close app: " + target + "; distance: " + distance);

        if (target != null) {
            dir = findBest(board, headX, headY, target);
        } else {
            for (Direction possibleDir : possibleDirs(dir)) {
                int[] nextCoord = nextCoord(possibleDir, headX, headY);
                if (canMove(board, nextCoord[0], nextCoord[1])) {
                    dir = possibleDir;
                    break;
                }
            }
        }

        return dir;
    }

    private static boolean canMove(Board board, int headX, int headY) {
        return !isBarrierAt(board, headX, headY);
    }

    private Direction findBest(Board board, int headX, int headY, Point target) {
        DirectionManhattan best = findBest(board, headX, headY, target, dir, 0, 3);
        if (best != null) {
            return best.direction;
        }
        return dir;
    }

    private static DirectionManhattan findBest(Board board, int headX, int headY, Point target, Direction curDir,
            int manhattanSum, int depth) {
        if (depth == 0) {
            return new DirectionManhattan(manhattanSum, curDir);
        }
        depth--;
        DirectionManhattan best = null;
        for (Direction possibleDir : possibleDirs(curDir)) {
            int[] nextCoord = nextCoord(possibleDir, headX, headY);
            if (canMove(board, nextCoord[0], nextCoord[1])) {
                int manhattan = manhattan(nextCoord[0], nextCoord[1], target.getX(), target.getY());
                if (manhattan == 0) {
                    depth = 0;
                }
                DirectionManhattan candidate = findBest(board, nextCoord[0], nextCoord[1], target, possibleDir,
                        manhattanSum + manhattan, depth);
                if (candidate != null) {
                    candidate.direction = possibleDir;
                    if (best == null) {
                        best = candidate;
                    } else if (candidate.manhattan < best.manhattan) {
                        best = candidate;
                    }
                }
            }
        }
        return best;
    }

    private static int[] nextCoord(Direction possibleDir, int headX, int headY) {
        int nextX = headX;
        int nextY = headY;
        switch (possibleDir) {
            case RIGHT:
                nextX = nextX + 1;
                break;
            case LEFT:
                nextX = nextX - 1;
                break;
            case UP:
                nextY = nextY + 1;
                break;
            case DOWN:
                nextY = nextY - 1;
                break;
        }
        return new int[] {nextX, nextY};
    }

    private static boolean isBarrierAt(Board board, int x, int y) {
        int size = board.size();
        boolean barrier = board.isBarrierAt(x, y) || board.isStoneAt(x, y) || x <= 1 || y <= 0 || x > size || y > size;
        Elements e = board.getAt(x, y);
        barrier = barrier || BARRIER_ELEMS.contains(e);
        // System.out.println("Barrier at [" + x + ", " + y + "]: " + barrier);
        return barrier;
    }

    private static Set<Direction> possibleDirs(Direction d) {
        EnumSet<Direction> directions = EnumSet.of(DOWN, RIGHT, UP, LEFT);
        switch(d) {
            case DOWN:
                directions.remove(UP);
                break;
            case RIGHT:
                directions.remove(LEFT);
                break;
            case UP:
                directions.remove(DOWN);
                break;
            case LEFT:
                directions.remove(RIGHT);
                break;
        }
        return directions;
    }

    /**
     * Calculate Manhattan distance.
     */
    private static int manhattan(int srcX, int srcY, int destX, int destY) {
        return Math.abs(srcX - destX) + Math.abs(srcY - destY);
    }

    private static final class DirectionManhattan {

        private final int manhattan;
        private Direction direction;

        private DirectionManhattan(int manhattan, Direction direction) {
            this.manhattan = manhattan;
            this.direction = direction;
        }
    }
}
