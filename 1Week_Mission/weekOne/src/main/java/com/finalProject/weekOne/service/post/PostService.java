package com.finalProject.weekOne.service.post;

import com.finalProject.weekOne.domain.app.util.Ut;
import com.finalProject.weekOne.domain.member.Member;
import com.finalProject.weekOne.domain.post.Post;
import com.finalProject.weekOne.domain.post.PostRepository;
import com.finalProject.weekOne.web.dto.post.CreatePostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    /** 새로운 게시물을 저장하는 메소드
     * @param member 현재 로그인된 Member
     * @param createPostDto subject, content가 포함된 DTO
     */
    public void savePost(Member member, CreatePostDto createPostDto) {
        Post newPost = Post.builder()
                .subject(createPostDto.getSubject())
                .content(createPostDto.getContent())
                .contentHtml(Ut.html.markdown(createPostDto.getContent()))
                .createDate(LocalDateTime.now())
                .author(member)
                .build();

        postRepository.save(newPost);
    }
}
