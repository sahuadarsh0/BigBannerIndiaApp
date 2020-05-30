package minds.technited.bigbannerindia;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import minds.technited.bigbannerindia.models.Category;

public class HomeActivityViewModel extends ViewModel {

    MutableLiveData<List<Category>>
            category = new MutableLiveData<>();

    public void setCategory(List<Category> category) {
        this.category.setValue(category);
    }
}
