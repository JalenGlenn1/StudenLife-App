package com.example.studentlife

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.w3c.dom.Text
import java.util.*
import kotlin.math.roundToLong


private const val TAG = "QuizListFragment"
class QuizListFragment : Fragment() {

    private var quizAdapter = QuizAdapter(emptyList())
    private lateinit var quizScore:TextView



    private lateinit var quizRecyclerView: RecyclerView

     interface Callbacks {
        fun onQuizSelect(uuid: UUID)
    }
    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quiz_list, container, false)
        quizRecyclerView = view.findViewById(R.id.quiz_recycler_view)
        quizRecyclerView.adapter = quizAdapter
        quizRecyclerView.layoutManager = LinearLayoutManager(context)
        quizScore = view.findViewById(R.id.your_score)
        QuizListViewModel().getAvgScore().observe(viewLifecycleOwner, {score -> score.let {

            quizScore.text = "Your Score  ${score.toInt()}"
        }
        })
      //  quizScore.text = "Your Score  ${score}"

        Log.d(TAG, "Got  quizzes")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        QuizListViewModel().getQuizzes().observe(
            viewLifecycleOwner,
            {quizzes -> quizzes.let {
                Log.d(TAG, "Got ${quizzes.size} quizzes")
                updateUI(quizzes)
            }
            }
        )
    }

    private fun updateUI(quizzes: List<Quiz>) {
        quizAdapter =  QuizAdapter(quizzes)
        quizRecyclerView.adapter = quizAdapter
    }

    companion object {
        fun newInstance(): QuizListFragment {
            return QuizListFragment()
        }
    }

  private  inner class QuizHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

     val cardTitle:TextView = view.findViewById(R.id.card_title)
     val cardDate:TextView = view.findViewById(R.id.card_date)
      val cardSubject:Chip = view.findViewById(R.id.card_subject)
      val cardMode:Chip = view.findViewById(R.id.card_mode)
      private lateinit var quiz: Quiz

      init {

          view.setOnClickListener(this)
      }

      fun bind(quiz: Quiz,num:String) {

          this.quiz = quiz
          cardTitle.text = num + "# Quiz " + quiz.score
          cardDate.text = quiz.date.toString()
          cardMode.text = quiz.mode
          cardSubject.text = quiz.subject


      }

      override fun onClick(p0: View?) {

              callbacks?.onQuizSelect(quiz.id)
      }
  }

    private inner class QuizAdapter(private val dataSet: List<Quiz>) : RecyclerView.Adapter<QuizHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.quiz_row_item,parent,false)

            return QuizHolder(view)
        }

        override fun onBindViewHolder(holder: QuizHolder, position: Int) {

            val  quiz = dataSet[position]
            var num = (position + 1).toString()
            holder.bind(quiz,num)

        }

        override fun getItemCount(): Int {

            return dataSet.size
        }

    }

}