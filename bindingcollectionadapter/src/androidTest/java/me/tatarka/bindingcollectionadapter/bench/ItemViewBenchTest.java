package me.tatarka.bindingcollectionadapter.bench;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Semaphore;

import io.leocad.delta.BenchmarkResult;
import io.leocad.delta.Delta;
import me.tatarka.bindingcollectionadapter.EmptyActivity;
import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Tests different possible ways to implement selecting an {@link ItemView}. It appears that the
 * current method of using a setter is the fastest.
 * <p/>
 * Results on a 2013 motox running lollipop 5.1:
 * <pre>
 * ItemView.set():
 *     avg warmup time: 394.140089ns
 *     avg bench time:  394.140089ns
 * ItemView.pool():
 *     avg warmup time: 549.427743ns
 *     avg bench time:  549.427743ns
 * ItemView.pack():
 *     avg warmup time: 841.477187ns
 *     avg bench time:  841.477187ns
 * ItemView.of():
 *     avg warmup time: 864.916832ns
 *     avg bench time:  864.916832ns
 * </pre>
 */
@Ignore
@RunWith(AndroidJUnit4.class)
public class ItemViewBenchTest {

    @Rule
    public ActivityTestRule<EmptyActivity> activityTestRule = new ActivityTestRule<>(EmptyActivity.class);

    private static final int ITERATION_COUNT = 1000000;
    private Semaphore semaphore = new Semaphore(0);

    /**
     * Select the item view by using a setter
     */
    @Test
    public void itemViewSetBench() {
        System.out.println("ItemView.set(): ");
        EmptyActivity activity = activityTestRule.getActivity();
        new Delta() {
            @Override
            public void onPostExecute(BenchmarkResult benchmarkResult) {
                System.out.println("    avg warmup time: " + benchmarkResult.benchmarkAvgTaskTimeNs + "ns");
                System.out.println("    avg bench time:  " + benchmarkResult.benchmarkAvgTaskTimeNs + "ns");
                semaphore.release();
            }
        }.benchmark(activity, ItemView1Bench.class, ITERATION_COUNT);
        semaphore.acquireUninterruptibly();
    }

    /**
     * Select the item view by constructing a new instance
     */
    @Test
    public void itemViewOfBench() {
        System.out.println("ItemView.of(): ");
        EmptyActivity activity = activityTestRule.getActivity();
        new Delta() {
            @Override
            public void onPostExecute(BenchmarkResult benchmarkResult) {
                System.out.println("    avg warmup time: " + benchmarkResult.benchmarkAvgTaskTimeNs + "ns");
                System.out.println("    avg bench time:  " + benchmarkResult.benchmarkAvgTaskTimeNs + "ns");
                semaphore.release();
            }
        }.benchmark(activity, ItemView2Bench.class, ITERATION_COUNT);
        semaphore.acquireUninterruptibly();
    }

    /**
     * Select the item view by packing it into a long.
     */
    @Test
    public void itemViewPackBench() {
        System.out.println("ItemView.pack(): ");
        EmptyActivity activity = activityTestRule.getActivity();
        new Delta() {
            @Override
            public void onPostExecute(BenchmarkResult benchmarkResult) {
                System.out.println("    avg warmup time: " + benchmarkResult.benchmarkAvgTaskTimeNs + "ns");
                System.out.println("    avg bench time:  " + benchmarkResult.benchmarkAvgTaskTimeNs + "ns");
                semaphore.release();
            }
        }.benchmark(activity, ItemView3Bench.class, ITERATION_COUNT);
        semaphore.acquireUninterruptibly();
    }

    /**
     * Select the item view by using an object pool.
     */
    @Test
    public void itemViewPoolBench() {
        System.out.println("ItemView.pool(): ");
        EmptyActivity activity = activityTestRule.getActivity();
        new Delta() {
            @Override
            public void onPostExecute(BenchmarkResult benchmarkResult) {
                System.out.println("    avg warmup time: " + benchmarkResult.benchmarkAvgTaskTimeNs + "ns");
                System.out.println("    avg bench time:  " + benchmarkResult.benchmarkAvgTaskTimeNs + "ns");
                semaphore.release();
            }
        }.benchmark(activity, ItemView4Bench.class, ITERATION_COUNT);
        semaphore.acquireUninterruptibly();
    }
}
