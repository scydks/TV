/*
 * Copyright (C) 2013 Chen Hui <calmer91@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package master.flame.danmaku.danmaku.model;


public class R2LDanmaku extends BaseDanmaku {

    protected float x = 0;

    protected float y = -1;

    protected int mDistance;

    protected float[] RECT = null;

    protected float mStepX;

    protected long mLastTime;

    public R2LDanmaku(Duration duration) {
        this.duration = duration;
    }

    @Override
    public void layout(IDisplay display, float x, float y) {
        if (mTimer != null) {
            long currMS = mTimer.currMillisecond;
            long deltaDuration = currMS - getActualTime();
            if (deltaDuration > 0 && deltaDuration < duration.value) {
                this.x = getAccurateLeft(display, currMS);
                if (!this.isShown()) {
                    this.y = y;
                    this.setVisibility(true);
                }
                mLastTime = currMS;
                return;
            }
            mLastTime = currMS;
        }
        this.setVisibility(false);
    }

    protected float getAccurateLeft(IDisplay display, long currTime) {
        long elapsedTime = currTime - getActualTime();
        if (elapsedTime >= duration.value) return -paintWidth;
        return display.getWidth() - elapsedTime * mStepX;
    }

    @Override
    public float[] getRectAtTime(IDisplay display, long time) {
        if (!isMeasured()) return null;
        float left = getAccurateLeft(display, time);
        if (RECT == null) RECT = new float[4];
        RECT[0] = left;
        RECT[1] = y;
        RECT[2] = left + paintWidth;
        RECT[3] = y + paintHeight;
        return RECT;
    }

    @Override
    public float getLeft() {
        return x;
    }

    @Override
    public float getTop() {
        return y;
    }

    @Override
    public float getRight() {
        return x + paintWidth;
    }

    @Override
    public float getBottom() {
        return y + paintHeight;
    }

    @Override
    public int getType() {
        return TYPE_SCROLL_RL;
    }

    @Override
    public void measure(IDisplay display, boolean fromWorkerThread) {
        super.measure(display, fromWorkerThread);
        mDistance = (int) (display.getWidth() + paintWidth);
        mStepX = mDistance / (float) duration.value;
    }
}