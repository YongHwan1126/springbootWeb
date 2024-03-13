/**
 * main.js
 */

$(document).ready(function(){
	console.log("정상 작동 확인!");
	
	// 제목 클릭시 이벤트 발생! click 이벤트 추가
	$('.showDetail').click(function(event){
		event.preventDefault(); // 링크의 기본 동작을 막는다.
		// console.log("클릭했다!");
		
		// 우리는 상세페이지를 모달에 띄우기 위해서 필요한 것
		// 실제 글의 번호를 가지고 와서 데이터베이스에 글의 번호를 이용해서 디비정보 하나만 가져와야한다.
		// 1. 클릭된 요소에서 부모요소를 찾는다.
		//    해당 되는 글의 번호를 가지고 온다.
		//    closest('태그')
		//    현재 나를 기준으로 가장 가까운 부모 태그를 찾는다. 
		var boardNo = $(this).closest('tr').find('td:first-child').text().trim();
		
		// 대부분 요소값에서 데이터를 꺼내올 때 특별한 일이 없으면 문자형태로 가져온다.
		// 우리가 저장했던 DB에 Long id값과 타입이 맞지 않아서 타입에러가 난다.
		// 문자 -> 숫자형태로 변경
		
		boardNo = parseInt(boardNo);
		// console.log(boardNo);
		// console.log(typeof boardNo);
		
		// AJAX 요청을 통해서 상세 페이지의 내용을 가져온다.
		// 헤더정보와 정보를 객체 형식으로 보낸다.
		// RestController, Controller랑 url충돌이 나지 않도록 조심해야 된다.
		$.ajax({
			url:'board/detailModal/'+boardNo,
			type: 'GET',
			dataType: 'html',
			// 성공시 응답받는 객체가 넘어온다.
			success : function(response){
				// 성공적으로 데이터를 받아온 경우 실행되는 함수
				// console.log(typeof response);
				// 문자(String) -> 객체(Object)로 변경
				var obj = JSON.parse(response);
				console.log(obj);
				console.log(typeof obj);
				
				// .html()
				// 매개변수는 문자열 형태의 html 콘텐츠를 받는다.
				// 위에 가져온 객체를 html 테이블 형식으로 문자로 연결해서 만들어주면 된다.
				// 
				
				var htmlContent = '<table class="table table-boardered">';
				htmlContent += '<tr><th>글번호</th><td>'+ obj.no +'</td></tr>';
				htmlContent += '<tr><th>제목</th><td>'+ obj.title +'</td></tr>';
				htmlContent += '<tr><th>작성자 번호</th><td>'+ obj.writerNo +'</td></tr>';
				htmlContent += '<tr><th>내용</th><td>'+ obj.content +'</td></tr>';
				htmlContent += '<tr><th>조회수</th><td>'+ obj.readCount +'</td></tr>';
				htmlContent += '<tr><th>작성시간</th><td>'+ obj.createDate +'</td></tr>';
				// 이하 필요한 데이터를 추가로 표시하도록 수정한다.
				htmlContent += '</table>'
					
				// 모달의 요소값을 가져온다.
				$('#modalContent').html(htmlContent);
				
				// 모달을 연다.
				$('#myModal').modal('show');
			},
			error : function (xhr, stat, error){
				// 에러발생시 실행되는 함수
				console.error(xhr.responseText);
			}
			
		}); // ajax끝
		
	}); // event끝
	
	// 게시글 검색버튼을 클릭했을 때 실행하는 이벤트
	$('#searchBtn').click(function(){
		
		console.log("게시글 검색버튼클릭")
		event.preventDefault(); 
		
		//전체 게시글을 가져오는 ajax만 보내면 되기 때문에..
		$.ajax({
			url: '/board/all',
			type: 'GET', // 전송방식 get
			dataType:'json', // json 타입으로 설정
			success : function(response){
				console.log(response);
				console.log(typeof response);
				var htmlContent = '<table class="table table-bordered">';
	            htmlContent += '<thead>';
	            htmlContent += '<tr>';
	            htmlContent += '<th scope="col">글번호</th>';
	            htmlContent += '<th scope="col">제목</th>';
	            htmlContent += '<th scope="col">작성자 번호</th>';
	            htmlContent += '<th scope="col">내용</th>';
	            htmlContent += '<th scope="col">조회수</th>';
	            htmlContent += '<th scope="col">작성 시간</th>';
	            htmlContent += '</tr>';
	            htmlContent += '</thead>';
	            htmlContent += '<tbody>';
	            // response 배열인 경우에는 대비하여 forEach()를 이용해서 각 객체를 처리한다.
	            response.forEach(function(data){
	            	htmlContent += '<tr>';
	            	htmlContent += '<td>'+ data.no + '</td>';
	            	htmlContent += '<td>'+ data.title + '</td>';
	            	htmlContent += '<td>'+ data.writerNo + '</td>';
	            	htmlContent += '<td>'+ data.content + '</td>';
	            	htmlContent += '<td>'+ data.readCount+ '</td>';
	            	htmlContent += '<td>'+ data.createDate+ '</td>';
	            	htmlContent += '</tr>'
	            	
	            });
	            
	            htmlContent+='</tbody>';
	            htmlContent+='</table>';
	            
	         // 모달의 요소값을 가져온다.
				$('#modalContent').html(htmlContent);
				
				// 모달을 연다.
				$('#myModal').modal('show');
			},
			error : function (xhr, stat, error){
				// 에러발생시 실행되는 함수
				console.error(xhr.responseText);
			}
			
		}); // ajax끝
	}); // 이벤트 끝!
	
	// 모달창 안 검색창에 키보드로 작성 후 엔터치면 이벤트 발생
		$('#searchModalInput').keypress(function(event){
			// 엔터키를 눌렀을 때
			if(event.which === 13){
				event.preventDefault();
				
				var input = $(this).val();
				
				console.log(input);
				// 검색어로 입력된 값을 가져왔다. 그러면 검색된 값이 타이틀과 내용을 모두 검색해서 결과값을 돌려받는다.
				$.ajax({
					url:'board/searchModal', // 검색어를 처리하는 url
					type: 'POST',
					data:{keyword : input}, //검색어를 파라메터로 전달
					dataType: 'json',
					success : function(response){
						// 검색한 결과를 테이블 형식으로 구성하여 모달에 출력
						var htmlContent = '<table class="table table-bordered">';
			            htmlContent += '<thead>';
			            htmlContent += '<tr>';
			            htmlContent += '<th scope="col">글번호</th>';
			            htmlContent += '<th scope="col">제목</th>';
			            htmlContent += '<th scope="col">작성자 번호</th>';
			            htmlContent += '<th scope="col">내용</th>';
			            htmlContent += '<th scope="col">조회수</th>';
			            htmlContent += '<th scope="col">작성 시간</th>';
			            htmlContent += '</tr>';
			            htmlContent += '</thead>';
			            htmlContent += '<tbody>';
			            // 반복되는 내용들을 출력하는 바디를 만들어준다.
			            for(var i = 0; i<response.length; i++){
			            	htmlContent += '<tr>';
			            	htmlContent += '<td>'+ response[i].no + '</td>';
			            	htmlContent += '<td>'+ response[i].title + '</td>';
			            	htmlContent += '<td>'+ response[i].writerNo + '</td>';
			            	htmlContent += '<td>'+ response[i].content + '</td>';
			            	htmlContent += '<td>'+ response[i].readCount+ '</td>';
			            	htmlContent += '<td>'+ response[i].createDate+ '</td>';
			            	htmlContent += '</tr>'
			            }
			            htmlContent+='</tbody>';
			            htmlContent+='</table>';
			         // 모달의 요소값을 가져온다.
						$('#modalContent').html(htmlContent);
						
						// 모달을 연다.
						$('#myModal').modal('show');
					},
					error: function (xhr, stat, error){
						// 에러발생시 실행되는 함수
						console.error(xhr.responseText);
					}
				});//ajax끝!
			}
		});
		
	
	
});// js끝

	