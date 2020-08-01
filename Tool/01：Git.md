### Git

------

##### Git：分布式版本控制系统，控制代码版本，可回退，提交，合并

- 工作区（workspace）：修改的区域
- 暂存区（stage)：暂时存放增加的文件
- 版本库(repository)：仓库(目录)里面的所有文件被Git管理，每个文件的修改、删除，Git都能跟踪，以便任何时刻都可以追踪历史，或者在将来某个时刻可以"还原"
  -  .git：版本库
  -  .gitignore ：忽略提交的文件，只要把文件名放进去，如果不想提交可以按文件名注释
  - 例：# xxx.html ：以html结尾的文件不会提交到GIT上

##### 1：配置config文件：

使用Git时，你的个人信息会保存在config文件中，配置个人信息时有两个级别

1. --system：会把配置信息写到最高级别的配置文件中(Git安装目录/etc/config)，系统上每个用户和仓库都会用这个配置文件
2. --global：会把配置信息写到只针对当前用户的配置文件中(c:/User/用户名/.gitconfig)

- 查看git用户信息
  - git config --global --list    ：查看用户配置列表
  - git config user.name
  - git config user.email 
- 添加自己的信息
  - git config --global user.email "you@example.com"
  - git config --global user.name "Your Name"

##### 2：基础命令（支持linux命令）

1. git init
   - 初始化一个仓库，会增加 .git 的目录(默认隐藏)，这个目录是Git来跟踪管理版本库的
2. vim readme.txt
   - 打开文本编辑器，介绍项目文件，建立文件 :wq 保存退出
3. git add . 
   - 将文件添加到暂存区（stage）
4. git commit -m "本次提交的说明" 
   - git commit -a
     - 直接将工作区跟踪过的的内容添加到版本库中，会进入”键入模式“添加comment。
   - 将暂存区中的文件提交到当前分支，生成版本号(快照)，提交之后暂存区就清空啦。
   - git commit -a -m "comment"
     - 直接提交到版本库编写注释。
5. git status 
   - 查看仓库的状态
6. git diff 文件名
   - 查看工作区和暂存区的区别。
   - --- a/readme.txt
   - +++ b/readme.txt
   - @@ -3,7 +3,7 @@："-3,7"：减号表示变化前的文件，从第三行（包括）开始，连续7行
7. git diff -- stat  ：查看哪些文件，有多少行产生了改动.+:增加的行数,-减少的行数，总共多少行修改
8. git log
   - 查看日志,版本号，十六进制表示
   - 在Git中HEAD 表示当前版本，上一个版本是HEAD^,上上版本是HEAD^^,在往上HEAD~1
   - git log 指定分支名：可以查看指定分支的日志
9. git ref log
   - 记录每一次的命令，得到版本号,可以回退到未来的那个版
10. git checkout -- 文件名	
    - 可以把文件在工作区的修改全部撤销
11. git rm 文件名 ，并且 git commit -m "XXX"
    - 删除了版本库中的文件

##### 3：远程仓库：本地Git仓库和GitHub仓库之间的传输是通过SSH加密的，支持Https 和 SSH协议

1. ssh-keygen -t rsa -C "likang315@163com"
   - 在本地生成公钥私钥,然后把公钥添加到Github 或者Gitlab
2. git remote add origin https://github.com/likang315/Demo.git
   - 要关联一个远程库
3. git remote rm origin
   - 删除远程库
4. git remote show origin
   - 查看关联的远程库
5. git push origin 本地分支：远程分支
   - 推送代码到远端对应的分支上
6. git push origin 本地分支
   - 远端自动创建于本地分支同名分支
7. git push origin --delete 远程分支名
   - 删除远程分支名，本地分支没有影响
8. git checkout 分支名 
   - 切换到指定分支 
9. git checkout -b 分支名 
   - 新建一个分支，并跳转到新的分支上	
10. git branch
    - 查看所有分支，当前分支前有 *
11. git branch -d 分支名
    - 删除本地分支
12. git checkout -b 本地分支名 origin/远程分支名
    - 创建本地分支，把远程指定分支拉到此分支上
13. git pull origin 远端分支
    - 拉取远端分支的代码拉取到当前分支的工作目录然后合并
14. git pull origin 远端分支：本地分支
    - 会自动快速向前合并
15. git fetch origin 远端分支
    - 将远端分支拉取的版本库里，拉下来检查之后没有问题，再决定是否需要合并到当前分支的工作目录里
    - git merge FETCH_HEAD  ：将拉取下来的最新内容合并到当前所在的分支中
16. git branch -v
    - 查看本地分支对应的远端分支
17. git clone git@github.com:likang315/Demo.git
    - 克隆仓库

##### 4：分支命令

1. 合并分支时，请注意--no-ff参数，表示禁用Fast forward(快速合并)，本次合并会创建一个新的commit，所以加上-m参数，把commit描述写进去
2. 在合并分支时，如果当前分支和需要合并的分支都做了提交，则会产生冲突，git status 查看状态，找出冲突文件修改后在提交
3. git merge --no-ff -m "XXX" 分支名
4. git branch --set-upstream branch-name origin/branch-name
   - 关联本地分支和远程分支
5. git merge 分支名
   - 合并指定分支到当前分支
   - git merge scott/master rick/master tom/master
     - 多路合并，和分开三次合并意义一样
6. git rebase 指定分支名
   - 该命令会把你的当前分支里的每个提交(commit)取消掉，并且把它们临时保存为补丁(patch)(这些补丁放到".git/rebase"目录中)，然后把当前分支更新成最新的"origin"分支，最后把保存的这些patch应用到当前分支上，分支更新之后，它会指向这些新创建的提交(commit),而那些老的提交会被丢弃。
   - 相对于merge来说，丢弃掉了并行分支，合并为一个分支，间隙的插入到当前分支上。
   - 在合并过程中，可能会产生冲突，Git会停止rebase并会让你去解决冲突，在解决完冲突后，用"git-add"命令去更新这些内容的索引(index), 然后，你无需执行 git-commit只要执行：git rebase --continue，这样Git就会继续应用剩余的patch。
7. git rebase --abort
   - 任何时候，你可以用 --abort 参数来终止rebase的行动，并且当前分支会回到rebase开始前的状态。

##### 5：储藏

1. git stash "comment"
   - 保存你的本地修改到储藏(stash)中, 然后将你的工作目录和索引里的内容全部重置, 回到你当前所在分支的上次提交时的状态. 等你修复完bug后,执行'反储藏'(unstash)操作就可以回到之前的工作里。
2. git stash apply
   - 执行反储藏恢复到以前的工作状态。
3. git stash list
   - 多次使用储藏命令,　每执行一次就会把针对当前修改的‘储藏’(stash)添加到储藏队列中，使用此命令可以查看保存的储藏。
4. git stash apply stash@{1}
   - 该命令来使用在队列中的任意一个'储藏'(stashes)
5. git stash clear
   - 用来清空这个队列。

##### 6：treeish

1. SHA串：40个字节长来表示一个commit。
2. sha短名(Partial Sha)：是不重复的(unique)，git也会把‘sha短名’(Partial Sha)自动补全。

##### 7：多个父对象

1. 假设master是由a和b两个分支合并的,那么 **master^1` 是指分支a**, `master^2` 就是指分支b.
2. master~2：master所指向的提交对象的第一个父对象的第一个父对象，等价于 master^^

##### 8：检索

1. git grep -n "检索内容"
   - 仓库里每个使用"检索内容"的地方，-n 是指显示出行号。

##### 9：GIT的撤销、恢复

1. git reset --hard 版本号或者HEAD^
   - 回退到指定(上一个)的版本，版本号没必要写完全，自动检索
2. git reset --hard HEAD
   - 把你工作目录中所有未提交的内容清空，让工作目录回到上次提交时的状态(last committed state)
3. git checkout -- hello.rb
   - 把hello.rb这个文件单独从HEAD中签出并且把它恢复成未修改时的样子
4. git reset HEAD <文件名>
   - 可以把暂存区的修改撤销掉（unstage），重新放回工作区
5. git revert HEAD
   - 恢复到上一次commit的状态，并且需要你输入comment，其实现在他在暂存区，输入comment才创建新的commit。
