import org.junit.Test;
import org.junit.*;
import static org.mockito.Mockito.*;
import android.view.View;

import com.DrNankn.cleanwater.Activities.LoginActivity;

import static org.junit.Assert.*;

/**
 * Created by hardiksangwan on 4/11/17.
 */
public class AnimateVisibilityTest {
    @Test
    public void visibilitySetTest(){
        View mockView = mock(View.class);
        LoginActivity mActivity = mock(LoginActivity.class);
        mActivity.animateViewVisibility(mockView, true);
        assertEquals(mockView.getVisibility(), View.VISIBLE);
    }

    @Test
    public void visibilitySetTest2(){
        View mockView = mock(View.class);
        LoginActivity mActivity = mock(LoginActivity.class);
        mActivity.animateViewVisibility(mockView, true);
        assertEquals(mockView.getVisibility(), View.GONE);
    }

    @Test
    public void alphaSetTest(){
        View mockView = mock(View.class);
        LoginActivity mActivity = mock(LoginActivity.class);
        mActivity.animateViewVisibility(mockView, true);
        assertEquals(mockView.getAlpha(), 0, 0);
    }

    @Test
    public void alphaSetTest2(){
        View mockView = mock(View.class);
        LoginActivity mActivity = mock(LoginActivity.class);
        mActivity.animateViewVisibility(mockView, true);
        assertEquals(mockView.getAlpha(), 1, 0);
    }

}