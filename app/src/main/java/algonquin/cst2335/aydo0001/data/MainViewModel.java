package algonquin.cst2335.aydo0001.data;
// data survives rotation changes
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {


    public MutableLiveData<String> editString = new MutableLiveData("");

    public MutableLiveData<Boolean> onOrOff = new MutableLiveData<Boolean>(false);
}
