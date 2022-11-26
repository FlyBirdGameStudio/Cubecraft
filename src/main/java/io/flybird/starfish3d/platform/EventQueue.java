//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.flybird.starfish3d.platform;

class EventQueue {
    private int maxEvents = 32;
    private int eventCount = 0;
    private int currentEventPos = -1;
    private int nextEventPos = 0;

    EventQueue(int maxEvents) {
        this.maxEvents = maxEvents;
    }

    void add() {
        ++this.eventCount;
        if (this.eventCount > this.maxEvents) {
            this.eventCount = this.maxEvents;
        }

        ++this.nextEventPos;
        if (this.nextEventPos == this.maxEvents) {
            this.nextEventPos = 0;
        }

        if (this.nextEventPos == this.currentEventPos) {
            ++this.currentEventPos;
        }

        if (this.currentEventPos == this.maxEvents) {
            this.currentEventPos = 0;
        }

    }

    boolean next() {
        if (this.eventCount == 0) {
            return false;
        } else {
            --this.eventCount;
            ++this.currentEventPos;
            if (this.currentEventPos == this.maxEvents) {
                this.currentEventPos = 0;
            }

            return true;
        }
    }

    int getMaxEvents() {
        return this.maxEvents;
    }

    int getCurrentPos() {
        return this.currentEventPos;
    }

    int getNextPos() {
        return this.nextEventPos;
    }
}
