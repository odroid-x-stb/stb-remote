/**
 * Copyright (C) 2012 Sylvain Bilange, Fabien Fleurey <fabien@fleurey.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.enseirb.odroidx.remote_client.gestures;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class GestureManager extends SimpleOnGestureListener {

	private static final int SLIDE_MIN_DISTANCE = 120;
    private static final int SLIDE_VELOCITY_THRESHOLD = 200;
	
	private GestureHandler receiver;
	
	public GestureManager(GestureHandler receiver) {
		this.receiver = receiver;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (e1.getX() - e2.getX() > SLIDE_MIN_DISTANCE && Math.abs(velocityX) > SLIDE_VELOCITY_THRESHOLD) {
			receiver.slidingLeft();
		} else if (e2.getX() - e1.getX() > SLIDE_MIN_DISTANCE && Math.abs(velocityX) > SLIDE_VELOCITY_THRESHOLD) {
			receiver.slidingRight();
		} else if (e1.getY() - e2.getY() > SLIDE_MIN_DISTANCE && Math.abs(velocityY) > SLIDE_VELOCITY_THRESHOLD) {
			receiver.slidingUp();
		} else if (e2.getY() - e1.getY() > SLIDE_MIN_DISTANCE && Math.abs(velocityY) > SLIDE_VELOCITY_THRESHOLD) {
			receiver.slidingDown();
		}
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		receiver.singleTap();
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		receiver.doubleTap();
		return true;
	}
}
