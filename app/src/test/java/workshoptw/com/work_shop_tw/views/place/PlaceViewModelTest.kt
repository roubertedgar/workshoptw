package workshoptw.com.work_shop_tw.views.place

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import workshoptw.com.work_shop_tw.models.place.Place
import workshoptw.com.work_shop_tw.models.place.PlaceDAO
import workshoptw.com.work_shop_tw.util.RxSchedulerTestSetup

@RunWith(MockitoJUnitRunner::class)
class PlaceViewModelTest {
    @Mock
    lateinit var dao: PlaceDAO

    private lateinit var viewModel: PlaceViewModel

    @Before
    fun setUp() {
        RxSchedulerTestSetup.setupRxScheduler()
        viewModel = PlaceViewModel(dao)
    }

    @After
    fun tearDown() {
        RxSchedulerTestSetup.reset()
    }

    @Test
    fun completeWhenSavePlace() {
        viewModel.savePlace(Place("", ""))
                .test()
                .assertNoValues()
                .assertComplete()
    }

    @Test
    fun returnsAllSavedPlacesWhenGetAll() {
        whenever(dao.getAll()).thenReturn(Flowable.just(getPlaces()))

        viewModel.getAll().test()
                .assertValue {
                    it.size == 2
                }.assertComplete()
    }

    @Test
    fun emptyWhenAreNoItemsSaved() {
        whenever(dao.getAll()).thenReturn(Flowable.empty())

        viewModel.getAll().test()
                .assertNoValues()
                .assertComplete()
    }

    private fun getPlaces(): List<Place> =
            listOf(Place("", ""), Place("", ""))
}