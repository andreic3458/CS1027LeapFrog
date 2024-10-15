/*
 * @author Andrei Ciceu 251355626
 * CS1027
 * 03/20/2024
 * Code for determining frog path in given pond
 */

public class FrogPath {
    private Pond pond; //Pond

    /*
     * @param String filename for pond
     * Constructor
     */
    public FrogPath(String filename) {
        try {
            pond = new Pond(filename);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /*
     * @param Hexagon currCell
     * @returns Hexagon returns cell
     * Goes through all possible cells and returns first priority cell
     */
    public Hexagon findBest(Hexagon currCell) {
        Hexagon[] neighbours = new Hexagon[6];
        ArrayUniquePriorityQueue<Hexagon> pq = new ArrayUniquePriorityQueue<Hexagon>();
        for (int i = 0; i < neighbours.length; i++) {
            neighbours[i] = currCell.getNeighbour(i);
        }
        for (int i = 0; i < neighbours.length; i++) {
            if (neighbours[i] == null) {
                continue;
            }
            if (neighbours[i].isMarked()){
                continue;
            }
            boolean neighbourAlligator = checkAlligatorAdjacent(neighbours[i]);
            if (neighbourAlligator && !neighbours[i].isReedsCell()) {
                continue;
            } else if (neighbourAlligator && neighbours[i].isReedsCell()) {
                pq.add(neighbours[i], 10.0);
                continue;
            }
            try {
                int flies = ((FoodHexagon) neighbours[i]).getNumFlies();
                if (flies == 3) {
                    pq.add(neighbours[i], 0.0);
                } else if (flies == 2) {
                    pq.add(neighbours[i], 1.0);
                } else if (flies == 1) {
                    pq.add(neighbours[i], 2.0);
                } else if (flies == 0) {
                    pq.add(neighbours[i], 6.0);
                }
                continue;
            } catch (Exception e) {
                System.out.print("");
            }

            if (neighbours[i].isEnd()) {
                pq.add(neighbours[i], 3.0);
            } else if (neighbours[i].isLilyPadCell()) {
                pq.add(neighbours[i], 4.0);
            } else if (neighbours[i].isReedsCell()) {
                pq.add(neighbours[i], 5.0);
            } else if (neighbours[i].isWaterCell()) {
                pq.add(neighbours[i], 6.0);
            } else if (neighbours[i].isMudCell()) {
                continue;
            } else if (neighbours[i].isAlligator()) {
                continue;
            }
        }
        if (currCell.isLilyPadCell() || currCell.isStart()) {
            for (int i = 0; i < neighbours.length; i++) {
                if (neighbours[i] == null) {
                    continue;
                }
                for (int j = 0; j < 6; j++) {
                    if (neighbours[i].getNeighbour(j) == null) {
                        continue;
                    }
                    if (neighbours[i].getNeighbour(j).isMarked()){
                        continue;
                    }
                    double addedPriority = 0.0;
                    if (i == j) {
                        addedPriority = 0.5;
                    } else {
                        addedPriority = 1.0;
                    }
                    boolean neighbourAlligator = checkAlligatorAdjacent(neighbours[i].getNeighbour(j));

                    if (neighbourAlligator && !neighbours[i].getNeighbour(j).isReedsCell()) {
                        continue;
                    } else if (neighbourAlligator && neighbours[i].getNeighbour(j).isReedsCell()) {
                        pq.add(neighbours[i].getNeighbour(j), 10.0 + addedPriority);
                        continue;
                    }
                    try {
                        int flies = ((FoodHexagon) neighbours[i].getNeighbour(j)).getNumFlies();
                        if (flies == 3) {
                            pq.add((FoodHexagon) neighbours[i].getNeighbour(j), 0.0 + addedPriority);
                        } else if (flies == 2) {
                            pq.add((FoodHexagon) neighbours[i].getNeighbour(j), 1.0 + addedPriority);
                        } else if (flies == 1) {
                            pq.add((FoodHexagon) neighbours[i].getNeighbour(j), 2.0 + addedPriority);
                        } else if (flies == 0) {
                            pq.add((FoodHexagon) neighbours[i].getNeighbour(j), 6.0 + addedPriority);
                        }
                        continue;
                    } catch (Exception e) {
                        System.out.print("");
                    }

                    if (neighbours[i].getNeighbour(j).isEnd()) {
                        pq.add(neighbours[i].getNeighbour(j), 3.0 + addedPriority);
                    } else if (neighbours[i].getNeighbour(j).isLilyPadCell()) {
                        pq.add(neighbours[i].getNeighbour(j), 4.0 + addedPriority);
                    } else if (neighbours[i].getNeighbour(j).isReedsCell()) {
                        pq.add(neighbours[i].getNeighbour(j), 5.0 + addedPriority);
                    } else if (neighbours[i].getNeighbour(j).isWaterCell()) {
                        pq.add(neighbours[i].getNeighbour(j), 6.0 + addedPriority);
                    } else if (neighbours[i].getNeighbour(j).isMudCell()) {
                        continue;
                    } else if (neighbours[i].getNeighbour(j).isAlligator()) {
                        continue;
                    }

                }
            }
        }
        if (!pq.isEmpty()) {
            return pq.removeMin();
        } else {
            return null;
        }

    }

    /*
     * @returns String String for path taken
     * Finds best path for Frog and determines if it is possible for Freddy to reach the end
     */
    public String findPath() {
        String str = "";
        ArrayStack<Hexagon> S = new ArrayStack<Hexagon>();
        S.push(pond.getStart());
        pond.getStart().markInStack();
        int fliesEaten = 0;
        while (!S.isEmpty()) {
            Hexagon curr = S.peek();
            str += curr.toString() + " ";
            if (curr.isEnd()) {
                break;
            }
            if (curr instanceof FoodHexagon) {
                fliesEaten += ((FoodHexagon) curr).getNumFlies();
                ((FoodHexagon) curr).removeFlies();
            }
            Hexagon next = findBest(curr);
            if (next == null) {
                S.pop();
                curr.markOutStack();
            } else {
                S.push(next);
                next.markInStack();
            }
        }
        if (S.isEmpty()) {
            return "No solution";
        }
        return str + "ate " + fliesEaten + " flies";
    }

    /*
     * @param Hexagon cell to check near
     * @returns boolean checks if alligator near cell
     * Checks 1-cell near to see if there is an alligator near it
     */
    private boolean checkAlligatorAdjacent(Hexagon cell) {
        for (int i = 0; i < 6; i++) {
            if (cell.getNeighbour(i) == null) {
                continue;
            }
            if (cell.getNeighbour(i).isAlligator()) {
                return true;
            }
        }
        return false;
    }

}
