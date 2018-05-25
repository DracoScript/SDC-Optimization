import matplotlib.pyplot as plt
import os
import sys

# check if dir was provided

if (len(sys.argv) < 1):
    print 'missing parameter directory'
    sys.exit()

reverse = 0
if (len(sys.argv) > 2):
    reverse = sys.argv[2]



# List all files from input path

mypath = sys.argv[1]
onlyfiles = [f for f in os.listdir(mypath) if os.path.isfile(os.path.join(mypath, f))]
print 'Loading '+str(len(onlyfiles))+' files...'
print ''



# Create lists

sa_gen = []
sa_best = []
sa_timer = []

ga_gen = []
ga_best = []
ga_timer = []



# Read files

for file_name in onlyfiles:

    # Read data
    data = open(mypath+file_name)

    gen = []
    best = []
    timer = []

    for line in data:
        line = line.replace('\n', '')
        aux = line.split(' ')
        gen.append(aux[0])
        timer.append(aux[len(aux)-1])
        if (reverse):
            best.append(-aux[1])
        else:
            best.append(aux[1])

    # Store in Simulated Annealing lists
    if (file_name.find('_SA.stat') > 0):

        sa_gen.append(gen)
        sa_best.append(best)
        sa_timer.append(timer)


    # Store in Genetic Algorithm lists
    elif (file_name.find("_GA.stat") > 0):

        ga_gen.append(gen)
        ga_best.append(best)
        ga_timer.append(timer)



# PLOT Best Solution

sa_fit = 1
for i in range(len(sa_best)):
    plt.plot(sa_gen[i], sa_best[i], 'b-', alpha=0.1)
    if (int(sa_best[i][-1]) < int(sa_best[sa_fit][-1])):
        sa_fit = i
ga_fit = 1
for i in range(len(ga_best)):
    plt.plot(ga_gen[i], ga_best[i], 'r-', alpha=0.1)
    if (int(ga_best[i][-1]) < int(ga_best[ga_fit][-1])):
        ga_fit = i
plt.plot(sa_gen[sa_fit], sa_best[sa_fit], 'w-', alpha=0.5, linewidth=5)
plt.plot(sa_gen[sa_fit], sa_best[sa_fit], 'b-', alpha=0.75, linewidth=3)
plt.plot(ga_gen[ga_fit], ga_best[ga_fit], 'w-', alpha=0.5, linewidth=5)
plt.plot(ga_gen[ga_fit], ga_best[ga_fit], 'r-', alpha=0.75, linewidth=3)
plt.xlabel('generation')
plt.ylabel('fitness of best solution (days)')
leg = plt.legend(['Simulated Annealing', 'Genetic Algorithm'])
leg.legendHandles[0].set_color('blue')
leg.legendHandles[0].set_alpha(1)
leg.legendHandles[1].set_color('red')
leg.legendHandles[1].set_alpha(1)
plt.show()



# PLOT Timer
medtimer = 0
for i in range(len(sa_timer)):
    plt.plot(sa_gen[i], sa_timer[i], 'b-', alpha=0.1)
    medtimer = medtimer + int(sa_timer[i][-1])
for i in range(len(ga_timer)):
    plt.plot(ga_gen[i], ga_timer[i], 'r-', alpha=0.1)
    medtimer = medtimer + int(ga_timer[i][-1])
plt.plot(sa_gen[sa_fit], sa_timer[sa_fit], 'w-', alpha=0.5, linewidth=5)
plt.plot(sa_gen[sa_fit], sa_timer[sa_fit], 'b-', alpha=0.75, linewidth=3)
plt.plot(ga_gen[ga_fit], ga_timer[ga_fit], 'w-', alpha=0.5, linewidth=5)
plt.plot(ga_gen[ga_fit], ga_timer[ga_fit], 'r-', alpha=0.75, linewidth=3)
medtimer = medtimer / (len(sa_timer) + len(ga_timer))
plt.ylim(0, medtimer * 5)
plt.xlabel('generation')
plt.ylabel('time spent (ns)')
leg = plt.legend(['Simulated Annealing', 'Genetic Algorithm'])
leg.legendHandles[0].set_color('blue')
leg.legendHandles[0].set_alpha(1)
leg.legendHandles[1].set_color('red')
leg.legendHandles[1].set_alpha(1)
plt.show()