package com.unimelbit.teamcobalt.tourlist.ServerRequester;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * Created by awhite on 6/10/17.
 */

@RunWith(RobolectricTestRunner.class)
public class GetRequesterTest {

    GetRequest request;
    GetRequester requester;

    @Before
    public void setUp() throws Exception {
        this.request = mock(GetRequest.class);
        this.requester = new GetRequester(this.request);
    }

    @Test
    public void doInBackground() throws Exception {
        MockServer server = new MockServer(5000);
        server.start();
        String result = this.requester.doInBackground("http://localhost:5000");
        assertEquals("GET\n", result);
        server.stop();
    }

    @Test
    public void onPostExecute() throws Exception {
        requester.onPostExecute("result");
        verify(request).processResult("result");
    }

}