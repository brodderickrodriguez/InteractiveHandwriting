package group6.interactivehandwriting.common.network;

/**
 * Created by JakeL on 9/24/18.
 */

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NetworkServiceTest {
    @Test
    public void test_useAppContext() {
        Context appContext = InstrumentationRegistry.getTargetContext();
    }
}

