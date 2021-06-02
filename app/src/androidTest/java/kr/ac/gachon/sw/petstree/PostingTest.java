package kr.ac.gachon.sw.petstree;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PostingTest {
    // 로그인 정보 입력
    private String ID = ""; // 이메일 아이디
    private String PW = ""; // 비밀번호

    // 테스트 정보 입력
    private String board = "반려동물 일상 게시판"; // 게시판 이름
    private String name = ""; // 닉네임
    private String title = ""; // 제목
    private String body = ""; // 내용

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void postingTest() throws InterruptedException {
        Thread.sleep(5000); // 시작 로딩

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.iv_menu),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_toolbar),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageView.perform(click());
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.nav_login), withText("로그인"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.core.widget.NestedScrollView")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.login_id),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText(ID), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.login_password),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText(PW), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.login_btn), withText("로그인"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());

        Thread.sleep(5000); // 로그인 로딩

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.nav_community), withText(board),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.core.widget.NestedScrollView")),
                                        0),
                                4),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.plus_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fl_main),
                                        0),
                                4),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.write_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fl_main),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton2.perform(click());


        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.title),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        appCompatEditText3.perform(scrollTo(), replaceText("Posting Test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.contentsEditText),
                        childAtPosition(
                                allOf(withId(R.id.contentsLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                0)));
        appCompatEditText4.perform(scrollTo(), replaceText("Posting Test Body"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.save_btn), withText("���"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                1)));
        appCompatButton2.perform(scrollTo(), click());

        Thread.sleep(1000); // 글 작성 로딩

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.iv_menu),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_toolbar),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageView2.perform(click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.nav_community), withText(board),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.core.widget.NestedScrollView")),
                                        0),
                                4),
                        isDisplayed()));
        appCompatTextView3.perform(click());

        Thread.sleep(3000); // 글 불러오기 로딩

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycler_post_list),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction editText = onView(
                allOf(withId(R.id.post_title), withText(title),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        editText.check(matches(withText(title)));


        ViewInteraction textView = onView(
                allOf(withText(body),
                        withParent(allOf(withId(R.id.contentsLayout),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        textView.check(matches(withText(body)));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.publisher), withText(name),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        textView2.check(matches(withText(name)));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
