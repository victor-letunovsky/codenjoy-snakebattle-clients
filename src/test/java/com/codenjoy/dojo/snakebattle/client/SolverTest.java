package com.codenjoy.dojo.snakebattle.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Direction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author K.ilya
 *         Это пример для реализации unit-тестов твоего бота
 *         Необходимо раскомментировать существующие тесты, добиться их выполнения ботом.
 *         Затем создавай свои тесты, улучшай бота и проверяй что не испортил предыдущие достижения.
 */

public class SolverTest {

    private Solver<Board> ai;

    @Before
    public void setup() {
        ai = new YourSolver();
    }

    private Board board(String board) {
        return (Board) new Board().forString(board);
    }

    @Test
    public void should() {
        asertAI("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "╘►     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼", Direction.RIGHT);

        asertAI("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼╘►    ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼", Direction.RIGHT);

        asertAI("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼#╘►   ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼", Direction.RIGHT);

        asertAI("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼☼ ╘►  ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼", Direction.RIGHT);
    }

    private void asertAI(String board, @SuppressWarnings("SameParameterValue") Direction expected) {
        String actual = ai.get(board(board));
        assertEquals(expected.toString(), actual);
    }
}
